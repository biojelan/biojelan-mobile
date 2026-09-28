package id.biojelan.app.data.repository

import id.biojelan.app.data.remote.ApiClient
import id.biojelan.app.data.remote.ApiResult
import id.biojelan.app.data.remote.CreateTransactionRequest
import id.biojelan.app.data.remote.TransactionActionRequest
import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.data.remote.TransactionStatusDto
import io.ktor.http.HttpMethod
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

enum class TxStatus {
    Pending, Accepted, Cancelled, Unknown;

    companion object {
        fun from(raw: String): TxStatus = when (raw.trim().lowercase()) {
            "pending" -> Pending
            "accepted" -> Accepted
            "cancelled", "canceled" -> Cancelled
            else -> Unknown
        }
    }
}

val TransactionDto.txStatus: TxStatus get() = TxStatus.from(status)

/** Endpoint di transaction.md. */
class TransactionRepository(
    private val api: ApiClient,
    private val json: Json,
) {
    /** 1. POST /api/agen-transaction — Agen membuat transaksi (status awal: pending). */
    suspend fun createAsAgen(
        agenId: String,
        klienId: String,
        klienName: String,
        volumeLiter: Double,
        pricePerLiter: Long,
    ): ApiResult<TransactionDto> {
        val total = kotlin.math.round(volumeLiter * pricePerLiter).toLong()
        val body = json.encodeToString(
            CreateTransactionRequest.serializer(),
            CreateTransactionRequest(agenId, klienId.trim(), klienName.trim(), volumeLiter, pricePerLiter, total),
        )
        return api.call(HttpMethod.Post, "/api/agen-transaction", bodyJson = body) { data ->
            json.decodeFromJsonElement(TransactionDto.serializer(), requireNotNull(data))
        }
    }

    /**
     * 2. GET /api/klien-transaction/status — transaksi terbaru untuk Klien yang sedang login.
     * Dokumen tidak menjelaskan respons saat belum ada transaksi, jadi data kosong/bukan objek -> null.
     */
    suspend fun klienStatus(): ApiResult<TransactionDto?> =
        api.call(HttpMethod.Get, "/api/klien-transaction/status") { data -> decodeOptionalTransaction(data) }

    /** 3. POST /api/klien-transaction/accept */
    suspend fun accept(transactionId: String): ApiResult<TransactionStatusDto> = action("/api/klien-transaction/accept", transactionId)

    /** 4. POST /api/klien-transaction/cancel */
    suspend fun cancel(transactionId: String): ApiResult<TransactionStatusDto> = action("/api/klien-transaction/cancel", transactionId)

    /**
     * 5. GET /api/agen-transactions
     *
     * API-DOC menaruh `agen_id` di REQUEST BODY untuk method GET. Body pada GET tidak didukung engine HTTP
     * mobile (OkHttp menolaknya) dan tidak bisa dikirim dari fetch/browser, jadi app mengirimnya sebagai
     * query string `?agen_id=`. Backend perlu membaca `searchParams` (atau cukup pakai identitas dari token).
     */
    suspend fun agenTransactions(agenId: String): ApiResult<List<TransactionDto>> =
        api.call(HttpMethod.Get, "/api/agen-transactions", query = mapOf("agen_id" to agenId)) { data -> decodeList(data) }

    /** 6. GET /api/klien-transactions — sama seperti di atas, `klien_id` dikirim sebagai query string. */
    suspend fun klienTransactions(klienId: String): ApiResult<List<TransactionDto>> =
        api.call(HttpMethod.Get, "/api/klien-transactions", query = mapOf("klien_id" to klienId)) { data -> decodeList(data) }

    private suspend fun action(path: String, transactionId: String): ApiResult<TransactionStatusDto> {
        val body = json.encodeToString(TransactionActionRequest.serializer(), TransactionActionRequest(transactionId))
        return api.call(HttpMethod.Post, path, bodyJson = body) { data ->
            json.decodeFromJsonElement(TransactionStatusDto.serializer(), requireNotNull(data))
        }
    }

    private fun decodeList(data: JsonElement?): List<TransactionDto> =
        if (data is JsonArray) json.decodeFromJsonElement(ListSerializer(TransactionDto.serializer()), data)
        else emptyList()

    private fun decodeOptionalTransaction(data: JsonElement?): TransactionDto? =
        if (data is JsonObject && data.isNotEmpty()) json.decodeFromJsonElement(TransactionDto.serializer(), data)
        else null
}
