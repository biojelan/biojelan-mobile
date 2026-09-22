package id.biojelan.app.data.repository

import id.biojelan.app.data.local.SessionStore
import id.biojelan.app.data.remote.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

enum class UserRole { Klien, Agen }

/**
 * Peran ditentukan dari isi `GET /api/user`: objek `agen` hanya ada untuk akun Agen (lihat user.md).
 * `role_id` di dokumentasi selalu bernilai 7 untuk semua contoh sehingga belum bisa dijadikan patokan.
 */
val UserDto.role: UserRole get() = if (agen != null) UserRole.Agen else UserRole.Klien

sealed interface SessionState {
    /** Sedang memeriksa token tersimpan. */
    data object Loading : SessionState

    data object LoggedOut : SessionState

    /** Gagal memuat profil saat start (mis. offline dan belum ada cache). */
    data class StartupError(val message: String) : SessionState

    data class LoggedIn(val user: UserDto) : SessionState

    /**
     * Pengunjung yang memilih "Lihat sebagai tamu" dari layar Masuk.
     * Tidak ada token — hanya boleh melihat lokasi Agen (lihat GuestScreen). Backend belum
     * punya endpoint publik untuk data Agen, jadi permintaan data di mode ini akan gagal
     * 401 apa adanya sampai ada endpoint publik.
     */
    data object Guest : SessionState
}

/** Sumber kebenaran tunggal untuk status login + profil pengguna aktif. */
class SessionManager(
    private val store: SessionStore,
    private val json: Json,
) {
    private val _state = MutableStateFlow<SessionState>(SessionState.Loading)
    val state: StateFlow<SessionState> = _state.asStateFlow()

    val token: String? get() = store.token

    val currentUser: UserDto? get() = (_state.value as? SessionState.LoggedIn)?.user

    fun saveToken(token: String) {
        store.token = token
    }

    fun setLoggedIn(user: UserDto) {
        store.cachedUserJson = json.encodeToString(UserDto.serializer(), user)
        _state.value = SessionState.LoggedIn(user)
    }

    /** Dipakai setelah profil berubah (PATCH /api/user, refresh stok, dst). */
    fun updateUser(user: UserDto) = setLoggedIn(user)

    fun cachedUser(): UserDto? {
        val raw = store.cachedUserJson ?: return null
        return try {
            json.decodeFromString(UserDto.serializer(), raw)
        } catch (e: Exception) {
            null
        }
    }

    fun setLoading() {
        _state.value = SessionState.Loading
    }

    /** Dipanggil saat pengguna memilih "Lihat sebagai tamu" di layar Masuk. */
    fun setGuest() {
        _state.value = SessionState.Guest
    }

    fun setStartupError(message: String) {
        _state.value = SessionState.StartupError(message)
    }

    fun clear() {
        store.clear()
        _state.value = SessionState.LoggedOut
    }
}
