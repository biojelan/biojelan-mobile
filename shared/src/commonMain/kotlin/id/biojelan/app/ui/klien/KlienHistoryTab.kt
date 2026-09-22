package id.biojelan.app.ui.klien

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.biojelan.app.core.agenInitials
import id.biojelan.app.core.formatLiter
import id.biojelan.app.core.formatRelativeDateTime
import id.biojelan.app.core.formatDateTime
import id.biojelan.app.core.formatRupiah
import id.biojelan.app.core.formatRupiahCompact
import id.biojelan.app.core.formatNumber
import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.data.repository.TxStatus
import id.biojelan.app.data.repository.txStatus
import id.biojelan.app.ui.Viewer
import id.biojelan.app.ui.chipKind
import id.biojelan.app.ui.components.BioButton
import id.biojelan.app.ui.components.BioChip
import id.biojelan.app.ui.components.BioSheet
import id.biojelan.app.ui.components.BtnStyle
import id.biojelan.app.ui.components.CircleIconButton
import id.biojelan.app.ui.components.DetailRow
import id.biojelan.app.ui.components.EmptyBlock
import id.biojelan.app.ui.components.ErrorBlock
import id.biojelan.app.ui.components.LoadingBlock
import id.biojelan.app.ui.components.NoteBox
import id.biojelan.app.ui.components.NoteTone
import id.biojelan.app.ui.components.ScreenPad
import id.biojelan.app.ui.components.ScreenTopBar
import id.biojelan.app.ui.components.StatCard
import id.biojelan.app.ui.components.TxRow
import id.biojelan.app.ui.counterpartName
import id.biojelan.app.ui.icons.BioIcons
import id.biojelan.app.ui.label
import id.biojelan.app.ui.theme.BioColors
import id.biojelan.app.ui.theme.BioTheme

@Composable
fun KlienHistoryTab(state: KlienUiState, vm: KlienViewModel) {
    var selectedId by remember { mutableStateOf<String?>(null) }
    val agenName: (String) -> String? = { id -> state.agens.firstOrNull { it.agenId == id }?.name }

    val accepted = state.transactions.filter { it.txStatus == TxStatus.Accepted }
    val totalLiters = accepted.sumOf { it.volumeLiter }
    val totalValue = accepted.sumOf { it.totalPrice }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            ScreenTopBar("Riwayat", actions = {
                CircleIconButton(BioIcons.Refresh, onClick = vm::loadTransactions, contentDescription = "Muat ulang")
            })
        }
        item {
            Row(Modifier.padding(horizontal = ScreenPad), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(state.transactions.size.toString(), "Transaksi", Modifier.weight(1f))
                StatCard(formatNumber(totalLiters, 1) + " L", "Minyak terjual", Modifier.weight(1f))
                StatCard(formatRupiahCompact(totalValue), "Total diterima", Modifier.weight(1f))
            }
        }
        when {
            state.txLoading && state.transactions.isEmpty() -> item { LoadingBlock() }
            state.txError != null && state.transactions.isEmpty() -> item { ErrorBlock(state.txError, vm::loadTransactions) }
            state.transactions.isEmpty() -> item {
                EmptyBlock(BioIcons.Receipt, "Belum ada transaksi", "Transaksi dengan Agen akan muncul di sini.")
            }
            else -> items(state.transactions) { tx ->
                val name = tx.counterpartName(Viewer.Klien, agenName)
                TxRow(
                    avatar = agenInitials(name),
                    title = name,
                    subtitle = formatRelativeDateTime(tx.createdAt) + " · " + formatLiter(tx.volumeLiter),
                    amount = formatRupiah(tx.totalPrice),
                    statusText = tx.txStatus.label(Viewer.Klien),
                    statusKind = tx.txStatus.chipKind(),
                    modifier = Modifier.padding(horizontal = ScreenPad),
                    onClick = { selectedId = tx.transactionId },
                )
            }
        }
    }

    val selected = state.transactions.firstOrNull { it.transactionId == selectedId }
    if (selected != null) {
        KlienTxDetailSheet(
            tx = selected,
            agenName = selected.counterpartName(Viewer.Klien, agenName),
            busy = state.busyTxId == selected.transactionId,
            onAccept = { vm.accept(selected.transactionId) { selectedId = null } },
            onCancel = { vm.cancel(selected.transactionId) { selectedId = null } },
            onDismiss = { selectedId = null },
        )
    }
}

@Composable
private fun KlienTxDetailSheet(
    tx: TransactionDto,
    agenName: String,
    busy: Boolean,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
) {
    var confirmCancel by remember { mutableStateOf(false) }
    BioSheet("Detail transaksi", onDismiss) {
        DetailRow("ID Transaksi", tx.transactionId, mono = true)
        DetailRow("Tanggal", formatDateTime(tx.createdAt))
        DetailRow("Agen", agenName)
        DetailRow("Volume", formatLiter(tx.volumeLiter))
        DetailRow("Harga / liter", formatRupiah(tx.price))
        DetailRow("Total", formatRupiah(tx.totalPrice), mono = true, valueColor = BioColors.Primary)
        DetailRow("Status", tx.txStatus.label(Viewer.Klien), last = true, valueColor = when (tx.txStatus) {
            TxStatus.Cancelled -> BioColors.Rust
            TxStatus.Pending -> BioColors.AmberDeep
            else -> BioColors.Primary
        })

        if (tx.txStatus == TxStatus.Pending) {
            Spacer(Modifier.height(14.dp))
            NoteBox(
                "Periksa jumlah dan harga. Terima jika sudah sesuai dengan yang Anda serahkan ke Agen; batalkan jika tidak.",
                tone = NoteTone.Amber,
                icon = BioIcons.Info,
            )
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BioButton(
                    if (confirmCancel) "Ya, batalkan" else "Batalkan",
                    onClick = { if (confirmCancel) onCancel() else confirmCancel = true },
                    modifier = Modifier.weight(1f),
                    style = BtnStyle.Rust,
                    enabled = !busy,
                )
                BioButton("Terima", onAccept, Modifier.weight(1f), loading = busy, icon = BioIcons.Check)
            }
            if (confirmCancel) {
                Spacer(Modifier.height(8.dp))
                Text("Pembatalan tidak dapat diurungkan.", style = BioTheme.type.small, color = BioColors.Rust)
            }
        }
    }
}
