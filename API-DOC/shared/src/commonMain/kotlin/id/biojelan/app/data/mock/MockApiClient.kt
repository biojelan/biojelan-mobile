package id.biojelan.app.data.mock

import id.biojelan.app.data.remote.ApiResult
import id.biojelan.app.data.remote.CheckClientByEmailRequest
import id.biojelan.app.data.remote.CheckClientByPhoneRequest
import id.biojelan.app.data.remote.LoginRequest
import id.biojelan.app.data.remote.RegisterRequest
import id.biojelan.app.data.remote.TransactionStatusDto
import id.biojelan.app.data.remote.UserDto
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Fallback API client: meniru respons backend Laravel asli (`biojelan-be-dashboard`) dengan data
 * dummy dari [MockData]. Path & alur sudah dicocokkan ke routes/api.php + controller-nya, bukan
 * ke API-DOC/*.md lama.
 *
 * Dipanggil oleh `ApiClient` saat request ke server gagal (network error) dan
 * `AppConfig.ENABLE_FALLBACK` aktif. Semua method mengembalikan [ApiResult] supaya
 * bisa langsung dipakai sebagai pengganti respons asli.
 */
class MockApiClient(private val json: Json) {

    /**
     * Token yang sedang "aktif" di mock session. Di-set saat login/register mock berhasil,
     * dan dipakai untuk menentukan user mana yang sedang login.
     */
    private var activeToken: String? = null

    /** Handle request berdasarkan method + path, return ApiResult yang sesuai. */
    fun <T> handle(
        method: HttpMethod,
        path: String,
        token: String?,
        bodyJson: String?,
        parse: (JsonElement?) -> T,
    ): ApiResult<T> {
        // Simpan token terakhir yang dipakai untuk lookup user
        if (token != null) activeToken = token

        return try {
            val result = route(method, path, bodyJson)
            @Suppress("UNCHECKED_CAST")
            ApiResult.Success(result as T, "Mock data (offline fallback)")
        } catch (e: MockAuthException) {
            ApiResult.Failure(e.message ?: "Autentikasi gagal.", ApiResult.Kind.Server)
        } catch (e: Exception) {
            ApiResult.Failure("Mock handler error: ${e.message}", ApiResult.Kind.Server)
        }
    }

    private fun route(method: HttpMethod, path: String, bodyJson: String?): Any? = when {

        // ============ Authentication ============

        method == HttpMethod.Post && path == "/api/login" -> {
            val req = json.decodeFromString(LoginRequest.serializer(), bodyJson!!)
            val payload = MockData.authPayloadForEmail(req.email)
                ?: throw MockAuthException("Akun belum terdaftar.")
            if (req.password != MockData.MOCK_PASSWORD) throw MockAuthException("Kata sandi salah.")
            activeToken = MockData.credentials[req.email]
            payload
        }

        method == HttpMethod.Post && path == "/api/register" -> {
            val req = json.decodeFromString(RegisterRequest.serializer(), bodyJson!!)
            activeToken = MockData.TOKEN_KLIEN
            MockData.registerPayload(req.name, req.email)
        }

        method == HttpMethod.Delete && path == "/api/logout" -> Unit

        method == HttpMethod.Post && path == "/api/forgot-password" -> Unit

        method == HttpMethod.Patch && path == "/api/update-password" -> Unit

        // ============ User ============

        method == HttpMethod.Get && path == "/api/user" -> currentMockUser()

        method == HttpMethod.Patch && path == "/api/user" -> currentMockUser() // anggap update berhasil

        method == HttpMethod.Delete && path == "/api/user" -> Unit

        // ============ Agen List (publik, GET /api/user/agen) ============

        method == HttpMethod.Get && path.startsWith("/api/user/agen") -> MockData.agenList

        // ============ Agent transactions ============

        method == HttpMethod.Post && path == "/api/agent/transaction" -> {
            // Buat transaksi baru (return data yang masuk akal, status awal selalu PENDING)
            MockData.agenTransactions.first().copy(
                transactionId = "trx-clients-mock-${System.currentTimeMillis()}",
                status = "PENDING",
            )
        }

        method == HttpMethod.Get && path == "/api/agent/clients/transactions" -> MockData.agenTransactions

        method == HttpMethod.Post && path == "/api/agent/check-clients-email" -> {
            val req = json.decodeFromString(CheckClientByEmailRequest.serializer(), bodyJson!!)
            if (req.clientEmail == MockData.clientLookup.clientEmail) MockData.clientLookup
            else MockData.clientLookup.copy(isExist = false, clientId = "", clientName = "")
        }

        method == HttpMethod.Post && path == "/api/agent/check-clients-phone" -> {
            val req = json.decodeFromString(CheckClientByPhoneRequest.serializer(), bodyJson!!)
            if (req.clientPhone == MockData.clientLookup.clientPhone) MockData.clientLookup
            else MockData.clientLookup.copy(isExist = false, clientId = "", clientName = "")
        }

        method == HttpMethod.Post && matchesTransactionAction(path, "agent", null, "cancel") != null -> {
            TransactionStatusDto(transactionId = matchesTransactionAction(path, "agent", null, "cancel")!!, status = "CANCEL_REQUESTED")
        }

        // ============ Client transactions ============

        method == HttpMethod.Get && path == "/api/client/transactions" -> MockData.klienTransactions

        method == HttpMethod.Post && matchesTransactionAction(path, "client", null, "accept") != null -> {
            TransactionStatusDto(transactionId = matchesTransactionAction(path, "client", null, "accept")!!, status = "ACCEPTED")
        }

        method == HttpMethod.Post && matchesTransactionAction(path, "client", null, "reject") != null -> {
            TransactionStatusDto(transactionId = matchesTransactionAction(path, "client", null, "reject")!!, status = "REJECTED")
        }

        method == HttpMethod.Post && matchesTransactionAction(path, "client", null, "cancel-accept") != null -> {
            TransactionStatusDto(transactionId = matchesTransactionAction(path, "client", null, "cancel-accept")!!, status = "CANCELLED")
        }

        method == HttpMethod.Post && matchesTransactionAction(path, "client", null, "cancel-reject") != null -> {
            TransactionStatusDto(transactionId = matchesTransactionAction(path, "client", null, "cancel-reject")!!, status = "ACCEPTED")
        }

        // ============ Pickup (Driver) — endpoint fiktif, lihat Dtos.kt ============

        method == HttpMethod.Get && path == "/api/agen/pickup/status" -> MockData.agenPickupStatus

        // ============ Fallback ============
        else -> {
            // Endpoint tidak dikenal, return null supaya tidak crash (parse() hanya dipanggil di
            // ApiClient asli; jalur mock ini langsung cast, jadi null aman untuk tipe nullable).
            null
        }
    }

    /** Cocokkan path `/api/{role}/transaction/{id}/{action}`, kembalikan id-nya kalau cocok. */
    private fun matchesTransactionAction(path: String, role: String, unused: String?, action: String): String? {
        val prefix = "/api/$role/transaction/"
        val suffix = "/$action"
        if (!path.startsWith(prefix) || !path.endsWith(suffix)) return null
        val id = path.removePrefix(prefix).removeSuffix(suffix)
        return id.ifBlank { null }
    }

    private fun currentMockUser(): UserDto =
        activeToken?.let { MockData.userByToken[it] } ?: MockData.klienUser

    private class MockAuthException(message: String) : Exception(message)

    private object System {
        // Simple counter for mock transaction IDs (KMP-safe, no java.lang.System)
        private var counter = 0L
        fun currentTimeMillis(): Long = ++counter
    }
}
