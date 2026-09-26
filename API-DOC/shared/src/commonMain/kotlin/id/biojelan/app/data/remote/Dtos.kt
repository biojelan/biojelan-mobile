package id.biojelan.app.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * DTO 1:1 dengan backend Laravel asli (`biojelan-be-dashboard`), BUKAN lagi API-DOC/*.md — banyak
 * di antaranya ternyata beda jauh dari implementasi aslinya (path, field, bahkan alur bisnisnya).
 * Semua field diberi default supaya respons yang kurang lengkap tidak membuat parsing gagal.
 */

// ============================================================ Authentication

@Serializable
data class AuthPayload(
    val token: String,
    val name: String = "",
    val email: String = "",
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerialName("password_confirmation") val passwordConfirmation: String,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class UpdatePasswordRequest(
    val password: String,
    @SerialName("new_password") val newPassword: String,
    @SerialName("password_confirmation") val passwordConfirmation: String,
)

@Serializable
data class ForgotPasswordRequest(
    val email: String,
)

// ============================================================ User

/** Objek `agen` di GET/PATCH /api/user (hanya ada untuk akun Agen). */
@Serializable
data class AgenDto(
    @SerialName("agen_id") @Serializable(with = FlexibleStringSerializer::class)
    val agenId: String = "",
    val address: String = "",
    @Serializable(with = FlexibleDoubleSerializer::class) val latitude: Double = 0.0,
    @Serializable(with = FlexibleDoubleSerializer::class) val longitude: Double = 0.0,
    @SerialName("bank_name") val bankName: String = "",
    @SerialName("account_number") @Serializable(with = FlexibleStringSerializer::class)
    val accountNumber: String = "",
    @SerialName("open_at") val openAt: String = "",
    @SerialName("close_at") val closeAt: String = "",
    @SerialName("open_days") val openDays: List<String> = emptyList(),
    @SerialName("is_open") @Serializable(with = FlexibleBooleanSerializer::class)
    val isOpen: Boolean = false,
    @SerialName("stock_liter") @Serializable(with = FlexibleDoubleSerializer::class)
    val stockLiter: Double = 0.0,
)

/** GET /api/user. Field `password` dari respons sengaja TIDAK dipetakan (jangan pernah disimpan di app). */
@Serializable
data class UserDto(
    @SerialName("user_id") @Serializable(with = FlexibleStringSerializer::class)
    val userId: String = "",
    @SerialName("role_id") val roleId: Int = 0,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    @SerialName("is_verified") @Serializable(with = FlexibleBooleanSerializer::class)
    val isVerified: Boolean = false,
    @SerialName("is_active") @Serializable(with = FlexibleBooleanSerializer::class)
    val isActive: Boolean = true,
    val agen: AgenDto? = null,
)

/** Item di GET /api/user/agen — perhatikan key-nya `open_days` (beda dengan `open_day` di atas). */
@Serializable
data class AgenSummaryDto(
    @SerialName("agen_id") @Serializable(with = FlexibleStringSerializer::class)
    val agenId: String = "",
    @SerialName("role_id") val roleId: Int = 0,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    @Serializable(with = FlexibleDoubleSerializer::class) val latitude: Double = 0.0,
    @Serializable(with = FlexibleDoubleSerializer::class) val longitude: Double = 0.0,
    @SerialName("open_at") val openAt: String = "",
    @SerialName("close_at") val closeAt: String = "",
    @SerialName("is_open") @Serializable(with = FlexibleBooleanSerializer::class)
    val isOpen: Boolean = false,
    @SerialName("open_days") val openDays: List<String> = emptyList(),
)

/** Body PATCH /api/user. Field null tidak dikirim. `password` sengaja tidak ada — pakai /api/update-password.
 *  Saat pertama kali bikin profil Agen (role_id sudah 6 tapi belum ada `agen`), backend WAJIB semua
 *  field agen diisi sekaligus (address/latitude/longitude/bank_name/account_number/open_at/close_at/
 *  open_days) — kalau sudah ada, boleh kirim sebagian saja (partial update). */
@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val agen: UpdateAgenRequest? = null,
)

/** latitude/longitude divalidasi backend sebagai angka (`numeric`), bukan string. */
@Serializable
data class UpdateAgenRequest(
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("bank_name") val bankName: String? = null,
    @SerialName("account_number") val accountNumber: String? = null,
    @SerialName("open_at") val openAt: String? = null,
    @SerialName("close_at") val closeAt: String? = null,
    @SerialName("open_days") val openDays: List<String>? = null,
    @SerialName("is_open") val isOpen: Boolean? = null,
)

// ============================================================ Transaction (Agen<->Client)

/**
 * POST /api/agent/transaction. Backend cuma butuh salah satu dari `client_email`/`client_phone`
 * (bukan client_id!) plus volume — harga & total dihitung 100% server-side dari tabel prices yang
 * aktif. Kirim `guest.client@biojelan.id` di client_email untuk transaksi pembeli tanpa akun.
 */
@Serializable
data class CreateTransactionRequest(
    @SerialName("client_email") val clientEmail: String? = null,
    @SerialName("client_phone") val clientPhone: String? = null,
    @SerialName("volume_liter") val volumeLiter: Double,
    @SerialName("transaction_note") val transactionNote: String? = null,
)

/** Body POST /api/agent/check-clients-email. */
@Serializable
data class CheckClientByEmailRequest(@SerialName("client_email") val clientEmail: String)

/** Body POST /api/agent/check-clients-phone. */
@Serializable
data class CheckClientByPhoneRequest(@SerialName("client_phone") val clientPhone: String)

/** Respons kedua endpoint check-clients-* di atas — dipakai Agen buat validasi sebelum submit transaksi. */
@Serializable
data class ClientLookupDto(
    @SerialName("is_exist") @Serializable(with = FlexibleBooleanSerializer::class)
    val isExist: Boolean = false,
    @SerialName("client_id") @Serializable(with = FlexibleStringSerializer::class)
    val clientId: String = "",
    @SerialName("client_name") val clientName: String = "",
    @SerialName("client_email") val clientEmail: String = "",
    @SerialName("client_phone") val clientPhone: String = "",
)

/**
 * Satu model untuk semua respons transaksi. Nama pihak lawan beda per endpoint: sisi Agen dapat
 * `client_name` (di [AgentClientTransactionResource]), sisi Client dapat `agen_name` (di
 * [ClientTransactionResource]) — tidak pernah dua-duanya sekaligus di satu respons.
 */
@Serializable
data class TransactionDto(
    @SerialName("transaction_id") @Serializable(with = FlexibleStringSerializer::class)
    val transactionId: String = "",
    @SerialName("agen_id") @Serializable(with = FlexibleStringSerializer::class)
    val agenId: String = "",
    @SerialName("client_id") @Serializable(with = FlexibleStringSerializer::class)
    val clientId: String = "",
    @SerialName("client_name") val clientName: String = "",
    @SerialName("agen_name") val agenName: String = "",
    @SerialName("volume_liter") @Serializable(with = FlexibleDoubleSerializer::class)
    val volumeLiter: Double = 0.0,
    @Serializable(with = FlexibleLongSerializer::class) val price: Long = 0L,
    @SerialName("total_price") @Serializable(with = FlexibleLongSerializer::class)
    val totalPrice: Long = 0L,
    /** PENDING, ACCEPTED, REJECTED, CANCEL_REQUESTED, atau CANCELLED — lihat [TxStatus]. */
    val status: String = "",
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("updated_at") val updatedAt: String = "",
)

/** Respons accept/reject/cancel/cancel-accept/cancel-reject: hanya id + status baru. */
@Serializable
data class TransactionStatusDto(
    @SerialName("transaction_id") @Serializable(with = FlexibleStringSerializer::class)
    val transactionId: String = "",
    val status: String = "",
)

// ============================================================ Pickup (Driver) — TIDAK ADA DI BACKEND ASLI
// routes/api.php backend Laravel (biojelan-be-dashboard) sama sekali tidak punya rute /api/*pickup*
// atau /api/driver/*, dan tidak ada model Pickup/Driver. Kode di bawah ini aman dibiarkan (call
// selalu gagal lalu fitur Pickup di app cuma nyembunyiin diri — lihat AgenPickupTab), tapi jangan
// dianggap terhubung ke apa pun sampai backend beneran punya endpoint ini.

/**
 * GET /api/agen/pickup/status (pickup.md — dokumen ini tidak match ke backend asli sama sekali).
 */
@Serializable
data class PickupStatusDto(
    @SerialName("pickup_id") @Serializable(with = FlexibleStringSerializer::class)
    val pickupId: String = "",
    @SerialName("driver_id") @Serializable(with = FlexibleStringSerializer::class)
    val driverId: String = "",
    @SerialName("agen_id") @Serializable(with = FlexibleStringSerializer::class)
    val agenId: String = "",
    val date: String = "",
    /** ASSIGNED, OTW, COMPLETED, CANCELLED. */
    val status: String = "",
    @SerialName("updated_at") val updatedAt: String = "",
)

// ============================================================ Price — TIDAK ADA ROUTE DI BACKEND ASLI
// Modelnya (`App\Models\Price`, dipakai TransactionClient) ADA di backend, tapi tidak ada route
// `/api/price` di routes/api.php sama sekali — harga cuma dipakai internal saat hitung total
// transaksi, tidak pernah diekspos ke app. [ApiPriceProvider] tetap aman (call gagal -> fallback ke
// AppConfig.DEFAULT_PRICE_PER_LITER), tapi jangan berharap endpoint ini benar-benar merespons.

/** GET /api/price — belum ada di backend asli, lihat catatan di atas. */
@Serializable
data class PriceDto(
    @SerialName("price_id") @Serializable(with = FlexibleStringSerializer::class)
    val priceId: String = "",
    @SerialName("price_per_liter") @Serializable(with = FlexibleLongSerializer::class)
    val pricePerLiter: Long = 0L,
    /** Di backend asli nilainya AGENT atau CLIENT (dua harga berbeda, lihat App\PriceType). */
    @SerialName("price_type") val priceType: String = "",
    @SerialName("start_date") val startDate: String = "",
    @SerialName("end_date") val endDate: String? = null,
)
