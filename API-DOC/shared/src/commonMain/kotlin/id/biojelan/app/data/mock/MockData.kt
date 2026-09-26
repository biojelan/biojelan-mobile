package id.biojelan.app.data.mock

import id.biojelan.app.core.AppConfig
import id.biojelan.app.data.remote.AgenDto
import id.biojelan.app.data.remote.AgenSummaryDto
import id.biojelan.app.data.remote.AuthPayload
import id.biojelan.app.data.remote.ClientLookupDto
import id.biojelan.app.data.remote.PickupStatusDto
import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.data.remote.UserDto

/**
 * Data dummy realistis sesuai backend Laravel asli (`biojelan-be-dashboard`) — role_id 6 = Agen,
 * 7 = Klien (App\Models\User::AGEN_ROLE_ID / CLIENT_ROLE_ID), status transaksi 5 nilai
 * (App\ClientTransactionStatus).
 *
 * Dipakai oleh [MockApiClient] sebagai fallback saat backend belum bisa dihubungi. Dua akun tersedia:
 * - Klien: `klien@biojelan.id` / `password123`
 * - Agen:  `agen@biojelan.id`  / `password123`
 */
object MockData {

    // ========================= Credentials & Tokens =========================

    const val MOCK_PASSWORD = "password123"
    const val TOKEN_KLIEN = "mock-token-klien-001"
    const val TOKEN_AGEN = "mock-token-agen-001"

    /** email → token */
    val credentials = mapOf(
        "klien@biojelan.id" to TOKEN_KLIEN,
        "agen@biojelan.id" to TOKEN_AGEN,
    )

    // ========================= Users =========================

    val klienUser = UserDto(
        userId = "1",
        roleId = 7,
        name = "Budi Santoso",
        email = "klien@biojelan.id",
        phone = "081234567890",
        isVerified = true,
        isActive = true,
        agen = null, // Klien -> tidak ada objek agen
    )

    val agenUser = UserDto(
        userId = "2",
        roleId = 6,
        name = "Siti Nurhaliza",
        email = "agen@biojelan.id",
        phone = "081298765432",
        isVerified = true,
        isActive = true,
        agen = AgenDto(
            agenId = "2",
            address = "Jl. Merdeka No. 10, Cikini, Jakarta Pusat",
            latitude = -6.1862,
            longitude = 106.8399,
            bankName = "BCA",
            accountNumber = "1234567890",
            openAt = "08:00",
            closeAt = "17:00",
            openDays = listOf("senin", "selasa", "rabu", "kamis", "jumat"),
            isOpen = true,
            stockLiter = 120.5,
        ),
    )

    /** token → UserDto */
    val userByToken = mapOf(
        TOKEN_KLIEN to klienUser,
        TOKEN_AGEN to agenUser,
    )

    // ========================= Auth Payloads =========================

    fun authPayloadForEmail(email: String): AuthPayload? {
        val token = credentials[email] ?: return null
        val user = userByToken[token] ?: return null
        return AuthPayload(token = token, name = user.name, email = user.email)
    }

    /** Register di backend asli SELALU jadi Klien baru (role_id dipaksa 7), apapun yang dikirim app. */
    fun registerPayload(name: String, email: String): AuthPayload =
        AuthPayload(token = TOKEN_KLIEN, name = name, email = email)

    // ========================= Agen List (GET /api/user/agen, publik) =========================

    val agenList = listOf(
        AgenSummaryDto(
            agenId = "2",
            roleId = 6,
            name = "Siti Nurhaliza",
            phone = "081298765432",
            address = "Jl. Merdeka No. 10, Cikini, Jakarta Pusat",
            latitude = -6.1862,
            longitude = 106.8399,
            openAt = "08:00",
            closeAt = "17:00",
            isOpen = true,
            openDays = listOf("senin", "selasa", "rabu", "kamis", "jumat"),
        ),
        AgenSummaryDto(
            agenId = "5",
            roleId = 6,
            name = "Ahmad Rizky",
            phone = "081377889900",
            address = "Jl. Sudirman No. 25, Kebayoran Baru, Jakarta Selatan",
            latitude = -6.2297,
            longitude = 106.8083,
            openAt = "09:00",
            closeAt = "18:00",
            isOpen = true,
            openDays = listOf("senin", "selasa", "rabu", "kamis", "jumat", "sabtu"),
        ),
        AgenSummaryDto(
            agenId = "6",
            roleId = 6,
            name = "Dewi Lestari",
            phone = "081511223344",
            address = "Jl. Gajah Mada No. 5, Glodok, Jakarta Barat",
            latitude = -6.1490,
            longitude = 106.8171,
            openAt = "07:30",
            closeAt = "16:30",
            isOpen = false,
            openDays = listOf("senin", "rabu", "jumat"),
        ),
        AgenSummaryDto(
            agenId = "7",
            roleId = 6,
            name = "Rudi Hermawan",
            phone = "081644556677",
            address = "Jl. Pemuda No. 8, Pulo Gadung, Jakarta Timur",
            latitude = -6.1835,
            longitude = 106.8990,
            openAt = "08:00",
            closeAt = "15:00",
            isOpen = true,
            openDays = listOf("selasa", "kamis", "sabtu"),
        ),
    )

    // ========================= Client lookup (check-clients-email/phone) =========================

    val clientLookup = ClientLookupDto(
        isExist = true,
        clientId = "1",
        clientName = "Budi Santoso",
        clientEmail = "klien@biojelan.id",
        clientPhone = "081234567890",
    )

    // ========================= Transactions =========================
    // status pakai nilai App\ClientTransactionStatus: PENDING/ACCEPTED/REJECTED/CANCEL_REQUESTED/CANCELLED

    /** GET /api/client/transactions — daftar transaksi Klien mock ("klien@biojelan.id", user_id "1"). */
    val klienTransactions = listOf(
        TransactionDto(
            transactionId = "trx-clients-001",
            agenId = "2",
            clientId = "1",
            agenName = "Siti Nurhaliza",
            volumeLiter = 5.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 5 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "ACCEPTED",
            createdAt = "2026-09-20 10:30:00",
            updatedAt = "2026-09-20 11:00:00",
        ),
        TransactionDto(
            transactionId = "trx-clients-002",
            agenId = "5",
            clientId = "1",
            agenName = "Ahmad Rizky",
            volumeLiter = 3.5,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = (3.5 * AppConfig.DEFAULT_PRICE_PER_LITER).toLong(),
            status = "PENDING",
            createdAt = "2026-09-21 14:15:00",
            updatedAt = "2026-09-21 14:15:00",
        ),
        TransactionDto(
            transactionId = "trx-clients-003",
            agenId = "6",
            clientId = "1",
            agenName = "Dewi Lestari",
            volumeLiter = 2.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 2 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "CANCELLED",
            createdAt = "2026-09-19 09:00:00",
            updatedAt = "2026-09-19 09:30:00",
        ),
    )

    /** GET /api/agent/clients/transactions — daftar transaksi Agen mock ("agen@biojelan.id", agen_id "2"). */
    val agenTransactions = listOf(
        TransactionDto(
            transactionId = "trx-clients-001",
            agenId = "2",
            clientId = "1",
            clientName = "Budi Santoso",
            volumeLiter = 5.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 5 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "ACCEPTED",
            createdAt = "2026-09-20 10:30:00",
            updatedAt = "2026-09-20 11:00:00",
        ),
        TransactionDto(
            transactionId = "trx-clients-004",
            agenId = "2",
            clientId = "3",
            clientName = "Rina Wulandari",
            volumeLiter = 8.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 8 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "PENDING",
            createdAt = "2026-09-22 08:45:00",
            updatedAt = "2026-09-22 08:45:00",
        ),
        TransactionDto(
            transactionId = "trx-clients-005",
            agenId = "2",
            clientId = "4",
            clientName = "Hendra Wijaya",
            volumeLiter = 10.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 10 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "CANCEL_REQUESTED",
            createdAt = "2026-09-18 16:00:00",
            updatedAt = "2026-09-23 08:00:00",
        ),
    )

    // ========================= Pickup (Driver) — TIDAK ADA DI BACKEND ASLI, lihat Dtos.kt =========

    /** GET /api/agen/pickup/status — endpoint fiktif, dibiarkan untuk mode offline lama saja. */
    val agenPickupStatus = PickupStatusDto(
        pickupId = "pkp-mock-001",
        driverId = "driver-001",
        agenId = "2",
        date = "2026-09-23",
        status = "OTW",
        updatedAt = "2026-09-23 09:40:00",
    )
}
