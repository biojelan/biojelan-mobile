package id.biojelan.app.ui.agen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.biojelan.app.ui.account.AccountViewModel
import id.biojelan.app.ui.account.ProfileTab
import id.biojelan.app.ui.components.BioTabBar
import id.biojelan.app.ui.components.CollectMessages
import id.biojelan.app.ui.components.TabItem
import id.biojelan.app.ui.components.ToastHost
import id.biojelan.app.ui.components.rememberToastState
import id.biojelan.app.ui.icons.BioIcons
import id.biojelan.app.ui.theme.BioColors
import org.koin.compose.viewmodel.koinViewModel

private val tabs = listOf(
    TabItem("Beranda", BioIcons.Home),
    TabItem("Transaksi", BioIcons.Receipt),
    TabItem("Stok", BioIcons.Drop),
    TabItem("Profil", BioIcons.User),
)

/** Alur pengguna berperan Agen: satu layar dengan 4 tab. */
@Composable
fun AgenFlow(vm: AgenViewModel = koinViewModel(), account: AccountViewModel = koinViewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    val user by vm.user.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableStateOf(0) }
    var showNew by remember { mutableStateOf(false) }
    val toast = rememberToastState()

    CollectMessages(vm.messages, toast)
    CollectMessages(account.messages, toast)

    Box(Modifier.fillMaxSize().background(BioColors.Paper)) {
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f).statusBarsPadding()) {
                val current = user
                if (current != null) {
                    when (tab) {
                        0 -> AgenHomeTab(state, current, vm, onNewTransaction = { showNew = true }, onGoTo = { tab = it })
                        1 -> AgenTransactionsTab(state, vm, onNewTransaction = { showNew = true })
                        2 -> AgenStockTab(state, current, vm)
                        else -> ProfileTab(current, account)
                    }
                }
            }
            BioTabBar(tabs, selected = tab, onSelect = { tab = it })
        }
        ToastHost(toast, Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 84.dp))
    }

    if (showNew) {
        NewTransactionSheet(
            price = state.price,
            creating = state.creating,
            onSubmit = { klienId, name, volume ->
                vm.createTransaction(klienId, name, volume) {
                    showNew = false
                    tab = 1
                }
            },
            onDismiss = { showNew = false },
        )
    }
}
