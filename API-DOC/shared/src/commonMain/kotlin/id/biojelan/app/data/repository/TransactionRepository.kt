package id.biojelan.app.data.repository

import id.biojelan.app.data.remote.ApiClient
import id.biojelan.app.data.remote.ApiResult
import id.biojelan.app.data.remote.CheckClientByEmailRequest
import id.biojelan.app.data.remote.CheckClientByPhoneRequest
import id.biojelan.app.data.remote.ClientLookupDto
import id.biojelan.app.data.remote.CreateTransactionRequest
import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.data.remote.TransactionStatusDto
import io.ktor.http.HttpMethod
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

/**
 * Status transaksi di backend asli (`App\ClientTransactionStatus`) — 5 nilai, BUKAN 3 seperti
 * dugaan awal dari dokumentasi lama. Agen TIDAK BISA langsung batalin transaksi sepihak: `cancel`
 * cuma MENGAJUKAN pembatalan (-> CancelRequested), lalu Client yang final terima (-> Cancelled)
 * atau tolak (balik lagi -> Accepted).
 */
enum class TxStatus {
    Pending, Accepted, Rejected, CancelRequested, Cancelled, Unknown;

    companion object {
        fun from(raw: String): TxStatus = when (raw.trim().uppercase()) {
            "PENDING" -> Pending
            "ACCEPTED" -> Accepted
            "REJECTED" -> Rejected
            "CANCEL_REQUESTED" -> CancelRequested
            "CANCELLED", "CANCELED" -> Cancelled
            else -> Unknown
        }
    }
}

val TransactionDto.txStatus: TxStatus get() = TxStatus.from(status)

/**
 * Endpoint transaksi Agen<->Client di backend Laravel asli (`biojelan-be-dashboard`) — BUKAN lagi
 * transaction.md/transaction_agen.md/transaction_klien.md, semua path di bawah sudah dicek
 * langsung dari routes/api.php + controller-nya (`AgentClientTransactionController`,
 * `ClientTransactionController`), bukan dari dokumentasi yang ternyata banyak melesetnya.
 */
class TransactionRepository(
    private val api: ApiClient,
    private val json: Json,
) {
    // ============================================================ Agent

    /**
     * POST /api/agent/transaction — Agen catat transaksi baru (status awal: PENDING). Isi salah
     * satu dari [clientEmail]/[clientPhone] (bukan client_id) — harga & total dihitung server dari
     * harga aktif saat ini, app tidak pernah mengirim angka harga. Kalau Client belum terdaftar,
     * backend balas 422 "Client not registered." — kecuali email-nya persis `guest.client@biojelan.id`
     * (transaksi buat pembeli tanpa akun).
     */
    suspend fun create(
        clientEmail: String?,
        clientPhone: String?,
        volumeLiter: Double,
        note: String?,
    ): ApiResult<TransactionDto> {
        val body = json.encodeToString(
            CreateTransactionRequest.serializer(),
            CreateTransactionRequest(
                clientEmail = clientEmail?.trim()?.ifBlank { null },
                clientPhone = clientPhone?.trim()?.ifBlank { null },
                volumeLiter = volumeLiter,
                transactionNote = note?.trim()?.ifBlank { null },
            ),
        )
        return api.call(HttpMethod.Post, "/api/agent/transaction", bodyJson = body) { data ->
            json.decodeFromJsonElement(TransactionDto.serializer(), requireNotNull(data))
        }
    }

    /** GET /api/agent/clients/transactions — semua transaksi milik Agen yang sedang login. */
    suspend fun agentTransactions(): ApiResult<List<TransactionDto>> =
        api.call(HttpMethod.Get, "/api/agent/clients/transactions") { data -> decodeList(data) }

    /** POST /api/agent/check-clients-email — cek Client terdaftar sebelum submit transaksi. */
    suspend fun checkClientByEmail(email: String): ApiResult<ClientLookupDto> {
        val body = json.encodeToString(CheckClientByEmailRequest.serializer(), CheckClientByEmailRequest(email.trim()))
        return api.call(HttpMethod.Post, "/api/agent/check-clients-email", bodyJson = body) { data ->
            json.decodeFromJsonElement(ClientLookupDto.serializer(), requireNotNull(data))
        }
    }

    /** POST /api/agent/check-clients-phone — sama seperti di atas, berdasarkan nomor telepon. */
    suspend fun checkClientByPhone(phone: String): ApiResult<ClientLookupDto> {
        val body = json.encodeToString(CheckClientByPhoneRequest.serializer(), CheckClientByPhoneRequest(phone.trim()))
        return api.call(HttpMethod.Post, "/api/agent/check-clients-phone", bodyJson = body) { data ->
            json.decodeFromJsonElement(ClientLookupDto.serializer(), requireNotNull(data))
        }
    }

    /**
     * POST /api/agent/transaction/{id}/cancel — Agen MENGAJUKAN pembatalan (cuma valid dari status
     * PENDING atau ACCEPTED). Ini belum final; Client yang menentukan lewat [clientAcceptCancellation]
     * atau [clientRejectCancellation].
     */
    suspend fun agentRequestCancel(transactionId: String): ApiResult<TransactionStatusDto> =
        statusAction("/api/agent/transaction/$transactionId/cancel")

    // ============================================================ Client

    /** GET /api/client/transactions — semua transaksi milik Client, atau filter lewat [status] (mis. "PENDING"). */
    suspend fun clientTransactions(status: String? = null): ApiResult<List<TransactionDto>> =
        api.call(
            HttpMethod.Get,
            "/api/client/transactions",
            query = if (status != null) mapOf("status" to status) else emptyMap(),
        ) { data -> decodeList(data) }

    /** POST /api/client/transaction/{id}/accept — PENDING -> ACCEPTED. */
    suspend fun clientAccept(transactionId: String): ApiResult<TransactionStatusDto> =
        statusAction("/api/client/transaction/$transactionId/accept")

    /** POST /api/client/transaction/{id}/reject — PENDING -> REJECTED. */
    suspend fun clientReject(transactionId: String): ApiResult<TransactionStatusDto> =
        statusAction("/api/client/transaction/$transactionId/reject")

    /** POST /api/client/transaction/{id}/cancel-accept — CANCEL_REQUESTED -> CANCELLED (final). */
    suspend fun clientAcceptCancellation(transactionId: String): ApiResult<TransactionStatusDto> =
        statusAction("/api/client/transaction/$transactionId/cancel-accept")

    /** POST /api/client/transaction/{id}/cancel-reject — CANCEL_REQUESTED -> ACCEPTED (pembatalan ditolak). */
    suspend fun clientRejectCancellation(transactionId: String): ApiResult<TransactionStatusDto> =
        statusAction("/api/client/transaction/$transactionId/cancel-reject")

    // ============================================================ Helpers

    private suspend fun statusAction(path: String): ApiResult<TransactionStatusDto> =
        api.call(HttpMethod.Post, path) { data ->
            json.decodeFromJsonElement(TransactionStatusDto.serializer(), requireNotNull(data))
        }

    private fun decodeList(data: JsonElement?): List<TransactionDto> =
        if (data is JsonArray) json.decodeFromJsonElement(ListSerializer(TransactionDto.serializer()), data)
        else emptyList()
}
