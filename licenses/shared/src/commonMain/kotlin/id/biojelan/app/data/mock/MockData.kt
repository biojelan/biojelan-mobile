package id.biojelan.app.data.mock

import id.biojelan.app.core.AppConfig
import id.biojelan.app.data.remote.AgenDto
import id.biojelan.app.data.remote.AgenSummaryDto
import id.biojelan.app.data.remote.AuthPayload
import id.biojelan.app.data.remote.PickupStatusDto
import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.data.remote.UserDto

/**
 * Data dummy realistis sesuai API-DOC.
 *
 * Dipakai oleh [MockApiClient] sebagai fallback saat backend belum di-deploy.
 * Dua akun tersedia:
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
        agen = null, // Klien → tidak ada objek agen
    )

    val agenUser = UserDto(
        userId = "2",
        roleId = 7,
        name = "Siti Nurhaliza",
        email = "agen@biojelan.id",
        phone = "081298765432",
        isVerified = true,
        isActive = true,
        agen = AgenDto(
            agenId = "101",
            address = "Jl. Merdeka No. 10, Cikini, Jakarta Pusat",
            latitude = -6.1862,
            longitude = 106.8399,
            bankName = "BCA",
            accountNumber = "1234567890",
            openAt = "08:00",
            closeAt = "17:00",
            openDay = listOf("senin", "selasa", "rabu", "kamis", "jumat"),
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

    /** Register selalu sukses, anggap jadi Klien baru. */
    fun registerPayload(name: String, email: String): AuthPayload =
        AuthPayload(token = TOKEN_KLIEN, name = name, email = email)

    // ========================= Agen List =========================

    val agenList = listOf(
        AgenSummaryDto(
            agenId = "101",
            roleId = 7,
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
            agenId = "102",
            roleId = 7,
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
            agenId = "103",
            roleId = 7,
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
            agenId = "104",
            roleId = 7,
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

    // ========================= Transactions =========================

    val klienTransactions = listOf(
        TransactionDto(
            transactionId = "TXN-001",
            agenId = "101",
            klienId = "1",
            name = "Budi Santoso",
            klienName = "Budi Santoso",
            agenName = "Siti Nurhaliza",
            volumeLiter = 5.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 5 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "accepted",
            createdAt = "2026-09-20 10:30:00",
            updatedAt = "2026-09-20 11:00:00",
        ),
        TransactionDto(
            transactionId = "TXN-002",
            agenId = "102",
            klienId = "1",
            name = "Budi Santoso",
            klienName = "Budi Santoso",
            agenName = "Ahmad Rizky",
            volumeLiter = 3.5,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = (3.5 * AppConfig.DEFAULT_PRICE_PER_LITER).toLong(),
            status = "pending",
            createdAt = "2026-09-21 14:15:00",
            updatedAt = "2026-09-21 14:15:00",
        ),
        TransactionDto(
            transactionId = "TXN-003",
            agenId = "103",
            klienId = "1",
            name = "Budi Santoso",
            klienName = "Budi Santoso",
            agenName = "Dewi Lestari",
            volumeLiter = 2.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 2 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "cancelled",
            createdAt = "2026-09-19 09:00:00",
            updatedAt = "2026-09-19 09:30:00",
        ),
    )

    val agenTransactions = listOf(
        TransactionDto(
            transactionId = "TXN-001",
            agenId = "101",
            klienId = "1",
            name = "Budi Santoso",
            klienName = "Budi Santoso",
            agenName = "Siti Nurhaliza",
            volumeLiter = 5.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 5 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "accepted",
            createdAt = "2026-09-20 10:30:00",
            updatedAt = "2026-09-20 11:00:00",
        ),
        TransactionDto(
            transactionId = "TXN-004",
            agenId = "101",
            klienId = "3",
            name = "Rina Wulandari",
            klienName = "Rina Wulandari",
            agenName = "Siti Nurhaliza",
            volumeLiter = 8.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 8 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "pending",
            createdAt = "2026-09-22 08:45:00",
            updatedAt = "2026-09-22 08:45:00",
        ),
        TransactionDto(
            transactionId = "TXN-005",
            agenId = "101",
            klienId = "4",
            name = "Hendra Wijaya",
            klienName = "Hendra Wijaya",
            agenName = "Siti Nurhaliza",
            volumeLiter = 10.0,
            price = AppConfig.DEFAULT_PRICE_PER_LITER,
            totalPrice = 10 * AppConfig.DEFAULT_PRICE_PER_LITER,
            status = "accepted",
            createdAt = "2026-09-18 16:00:00",
            updatedAt = "2026-09-18 16:30:00",
        ),
    )

    /** Transaksi pending terbaru untuk Klien (dipakai di klien-transaction/status). */
    val klienLatestPending: TransactionDto? = klienTransactions.firstOrNull { it.status == "pending" }

    // ========================= Pickup (Driver) =========================

    /** Dipakai di GET /api/agen/pickup/status untuk Agen mock (agenId "101"). */
    val agenPickupStatus = PickupStatusDto(
        pickupId = "pkp-mock-001",
        driverId = "driver-001",
        agenId = "101",
        date = "2026-09-23",
        status = "OTW",
        updatedAt = "2026-09-23 09:40:00",
    )
}
