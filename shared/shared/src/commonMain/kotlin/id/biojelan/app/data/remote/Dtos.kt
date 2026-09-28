package id.biojelan.app.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * DTO 1:1 dengan API-DOC (authentication.md, user.md, transaction.md).
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
    @SerialName("new_password_confirmed") val newPasswordConfirmed: String,
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
    @SerialName("open_day") val openDay: List<String> = emptyList(),
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

/** Body PATCH /api/user. Field null tidak dikirim. `password` sengaja tidak ada — pakai /api/update-password. */
@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val agen: UpdateAgenRequest? = null,
)

/** Di dokumentasi, latitude/longitude pada REQUEST berupa string. */
@Serializable
data class UpdateAgenRequest(
    val address: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    @SerialName("bank_name") val bankName: String? = null,
    @SerialName("account_number") val accountNumber: String? = null,
    @SerialName("open_at") val openAt: String? = null,
    @SerialName("close_at") val closeAt: String? = null,
    @SerialName("open_day") val openDay: List<String>? = null,
    @SerialName("is_open") val isOpen: Boolean? = null,
)

// ============================================================ Transaction

@Serializable
data class CreateTransactionRequest(
    @SerialName("agen_id") val agenId: String,
    @SerialName("klien_id") val klienId: String,
    val name: String,
    @SerialName("volume_liter") val volumeLiter: Double,
    val price: Long,
    @SerialName("total_price") val totalPrice: Long,
)

@Serializable
data class TransactionActionRequest(
    @SerialName("transaction_id") val transactionId: String,
)

/**
 * Satu model untuk semua respons transaksi. Nama pihak lawan beda-beda per endpoint:
 * create/status -> `name`, agen list -> `klien_name`, klien list -> `agen_name`.
 */
@Serializable
data class TransactionDto(
    @SerialName("transaction_id") @Serializable(with = FlexibleStringSerializer::class)
    val transactionId: String = "",
    @SerialName("agen_id") @Serializable(with = FlexibleStringSerializer::class)
    val agenId: String = "",
    @SerialName("klien_id") @Serializable(with = FlexibleStringSerializer::class)
    val klienId: String = "",
    val name: String = "",
    @SerialName("klien_name") val klienName: String = "",
    @SerialName("agen_name") val agenName: String = "",
    @SerialName("volume_liter") @Serializable(with = FlexibleDoubleSerializer::class)
    val volumeLiter: Double = 0.0,
    @Serializable(with = FlexibleLongSerializer::class) val price: Long = 0L,
    @SerialName("total_price") @Serializable(with = FlexibleLongSerializer::class)
    val totalPrice: Long = 0L,
    val status: String = "",
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("updated_at") val updatedAt: String = "",
)

/** Respons accept/cancel: hanya id + status baru. */
@Serializable
data class TransactionStatusDto(
    @SerialName("transaction_id") @Serializable(with = FlexibleStringSerializer::class)
    val transactionId: String = "",
    val status: String = "",
)
