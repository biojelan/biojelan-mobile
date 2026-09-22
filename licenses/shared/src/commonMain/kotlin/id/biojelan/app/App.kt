package id.biojelan.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.biojelan.app.data.repository.SessionState
import id.biojelan.app.data.repository.UserRole
import id.biojelan.app.data.repository.role
import id.biojelan.app.ui.RootViewModel
import id.biojelan.app.ui.agen.AgenFlow
import id.biojelan.app.ui.auth.AuthFlow
import id.biojelan.app.ui.auth.SplashScreen
import id.biojelan.app.ui.guest.GuestScreen
import id.biojelan.app.ui.klien.KlienFlow
import id.biojelan.app.ui.theme.BioTheme
import org.koin.compose.viewmodel.koinViewModel

/**
 * Akar aplikasi. Layar yang tampil ditentukan sepenuhnya oleh [SessionState]:
 * belum masuk -> alur autentikasi; sudah masuk -> alur Klien atau Agen sesuai isi GET /api/user.
 */
@Composable
fun App() {
    BioTheme {
        val root: RootViewModel = koinViewModel()
        val state by root.state.collectAsStateWithLifecycle()

        when (val s = state) {
            is SessionState.Loading -> SplashScreen()
            is SessionState.StartupError -> SplashScreen(error = s.message, onRetry = root::retry)
            is SessionState.LoggedOut -> AuthFlow()
            is SessionState.Guest -> GuestScreen()
            is SessionState.LoggedIn -> when (s.user.role) {
                UserRole.Agen -> AgenFlow()
                UserRole.Klien -> KlienFlow()
            }
        }
    }
}
