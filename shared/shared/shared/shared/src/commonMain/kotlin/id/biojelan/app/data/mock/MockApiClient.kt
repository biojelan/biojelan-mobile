package id.biojelan.app.data.mock

import id.biojelan.app.data.remote.AgenSummaryDto
import id.biojelan.app.data.remote.ApiResult
import id.biojelan.app.data.remote.AuthPayload
import id.biojelan.app.data.remote.LoginRequest
import id.biojelan.app.data.remote.RegisterRequest
import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.data.remote.TransactionStatusDto
import id.biojelan.app.data.remote.UserDto
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Fallback API client: meniru respons backend dengan data dummy dari [MockData].
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

        method == HttpMethod.Get && path == "/api/user" -> {
            currentMockUser()
        }

        method == HttpMethod.Patch && path == "/api/user" -> {
            // Return user saat ini (anggap update berhasil)
            currentMockUser()
        }

        method == HttpMethod.Delete && path == "/api/user" -> Unit

        // ============ Agen List ============

        method == HttpMethod.Get && path == "/api/user/agen" -> {
            MockData.agenList
        }

        // ============ Transactions ============

        method == HttpMethod.Post && path == "/api/agen-transaction" -> {
            // Buat transaksi baru (return data yang masuk akal)
            MockData.agenTransactions.first().copy(
                transactionId = "TXN-MOCK-${System.currentTimeMillis()}",
                status = "pending",
            )
        }

        method == HttpMethod.Get && path == "/api/klien-transaction/status" -> {
            MockData.klienLatestPending
        }

        method == HttpMethod.Post && path == "/api/klien-transaction/accept" -> {
            TransactionStatusDto(transactionId = "TXN-002", status = "accepted")
        }

        method == HttpMethod.Post && path == "/api/klien-transaction/cancel" -> {
            TransactionStatusDto(transactionId = "TXN-002", status = "cancelled")
        }

        method == HttpMethod.Get && path == "/api/agen-transactions" -> {
            MockData.agenTransactions
        }

        method == HttpMethod.Get && path == "/api/klien-transactions" -> {
            MockData.klienTransactions
        }

        // ============ Fallback ============
        else -> {
            // Endpoint tidak dikenal, return empty supaya tidak crash
            null
        }
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
