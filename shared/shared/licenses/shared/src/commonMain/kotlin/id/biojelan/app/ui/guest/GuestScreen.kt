package id.biojelan.app.ui.guest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.biojelan.app.ui.components.BioButton
import id.biojelan.app.ui.components.BioSheet
import id.biojelan.app.ui.components.BioTabBar
import id.biojelan.app.ui.components.BtnStyle
import id.biojelan.app.ui.components.PriceBand
import id.biojelan.app.ui.components.ScreenPad
import id.biojelan.app.ui.components.TabItem
import id.biojelan.app.ui.icons.BioIcons
import id.biojelan.app.ui.klien.KlienLocatorTab
import id.biojelan.app.ui.klien.KlienUiState
import id.biojelan.app.ui.theme.BioColors
import id.biojelan.app.ui.theme.BioTheme
import org.koin.compose.viewmodel.koinViewModel

private const val LOCATOR_TAB_INDEX = 1

private val guestTabs = listOf(
    TabItem("Beranda", BioIcons.Home),
    TabItem("Cari Agen", BioIcons.Pin),
    TabItem("Riwayat", BioIcons.Receipt),
    TabItem("Profil", BioIcons.User),
)

/**
 * Alur untuk pengunjung yang memilih "Lihat sebagai tamu": boleh melihat lokasi Agen dan
 * harga jelantah terkini (tab Cari Agen). Tab lain dan baris Agen memicu modal ajakan daftar
 * (menyamai perilaku prototype HTML).
 */
@Composable
fun GuestScreen(vm: GuestViewModel = koinViewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    var showUpsell by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(BioColors.Paper)) {
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f).statusBarsPadding()) {
                Column(Modifier.fillMaxSize()) {
                    GuestBanner(onClick = vm::exitToRegister)
                    PriceBand(
                        price = state.price,
                        caption = "Harga jelantah hari ini · berlaku untuk semua Agen",
                        modifier = Modifier.padding(horizontal = ScreenPad, vertical = 4.dp),
                    )
                    Box(Modifier.weight(1f)) {
                        KlienLocatorTab(
                            state = KlienUiState(
                                agens = state.agens,
                                agensLoading = state.loading,
                                agensError = state.error,
                            ),
                            onRefresh = vm::loadAgens,
                            onOpenAgen = { showUpsell = true },
                        )
                    }
                }
            }
            BioTabBar(
                items = guestTabs,
                selected = LOCATOR_TAB_INDEX,
                onSelect = { index -> if (index != LOCATOR_TAB_INDEX) showUpsell = true },
            )
        }
    }

    if (showUpsell) {
        GuestUpsellSheet(
            onDismiss = { showUpsell = false },
            onRegister = {
                showUpsell = false
                vm.exitToRegister()
            },
        )
    }
}

@Composable
private fun GuestBanner(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ScreenPad, vertical = 10.dp)
            .background(BioColors.PrimaryTint, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(BioIcons.Eye, contentDescription = null, tint = BioColors.Primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text("Lihat sebagai tamu.", style = BioTheme.type.bodyBold, color = BioColors.Primary)
            Text(
                "Daftar untuk mulai menjual minyak jelantah dan melihat riwayat transaksi Anda. Ketuk untuk mendaftar.",
                style = BioTheme.type.small,
                color = BioColors.Primary,
            )
        }
    }
}

@Composable
private fun GuestUpsellSheet(onDismiss: () -> Unit, onRegister: () -> Unit) {
    BioSheet(title = "Daftar untuk lanjut", onDismiss = onDismiss) {
        Text(
            "Sebagai tamu, Anda hanya dapat melihat lokasi Agen di peta dan harga jelantah terkini. Daftar terlebih dahulu untuk mulai menjual minyak jelantah, melihat riwayat transaksi, dan mengatur profil.",
            style = BioTheme.type.body,
            color = BioColors.InkSoft,
        )
        Spacer(Modifier.height(18.dp))
        BioButton("Daftar Sekarang", onClick = onRegister, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        BioButton("Nanti dulu", onClick = onDismiss, style = BtnStyle.Outline, modifier = Modifier.fillMaxWidth())
    }
}
