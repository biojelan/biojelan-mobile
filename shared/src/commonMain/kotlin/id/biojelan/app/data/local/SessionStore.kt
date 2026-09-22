package id.biojelan.app.data.local

import com.russhwolf.settings.Settings

/**
 * Penyimpanan sesi di perangkat (SharedPreferences di Android, NSUserDefaults di iOS).
 *
 * CATATAN KEAMANAN: token disimpan tanpa enkripsi. Untuk rilis produksi sebaiknya pindah ke
 * Android Keystore / iOS Keychain (mis. lewat multiplatform-settings `KeychainSettings` + EncryptedSharedPreferences).
 */
class SessionStore(private val settings: Settings) {

    var token: String?
        get() = settings.getStringOrNull(KEY_TOKEN)
        set(value) {
            if (value == null) settings.remove(KEY_TOKEN) else settings.putString(KEY_TOKEN, value)
        }

    /** Salinan terakhir profil (JSON) — dipakai agar app tetap terbuka saat start dalam kondisi offline. */
    var cachedUserJson: String?
        get() = settings.getStringOrNull(KEY_USER)
        set(value) {
            if (value == null) settings.remove(KEY_USER) else settings.putString(KEY_USER, value)
        }

    fun clear() {
        settings.remove(KEY_TOKEN)
        settings.remove(KEY_USER)
    }

    private companion object {
        const val KEY_TOKEN = "session.token"
        const val KEY_USER = "session.user"
    }
}
