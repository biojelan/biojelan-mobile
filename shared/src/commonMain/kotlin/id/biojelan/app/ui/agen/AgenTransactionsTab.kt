package id.biojelan.app.ui.agen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import id.biojelan.app.core.AppConfig
import id.biojelan.app.core.formatDateTime
import id.biojelan.app.core.formatLiter
import id.biojelan.app.core.formatNumber
import id.biojelan.app.core.formatRelativeDateTime
import id.biojelan.app.core.formatRupiah
import id.biojelan.app.core.formatRupiahCompact
import id.biojelan.app.core.initialsOf
import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.data.repository.TxStatus
import id.biojelan.app.data.repository.txStatus
import id.biojelan.app.ui.Viewer
import id.biojelan.app.ui.chipKind
import id.biojelan.app.ui.components.BioButton
import id.biojelan.app.ui.components.BioField
import id.biojelan.app.ui.components.BioSheet
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
fun AgenTransactionsTab(state: AgenUiState, vm: AgenViewModel, onNewTransaction: () -> Unit) {
    var selectedId by remember { mutableStateOf<String?>(null) }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                ScreenTopBar("Transaksi", actions = {
                    CircleIconButton(BioIcons.Refresh, onClick = vm::refresh, contentDescription = "Muat ulang")
                })
            }
            item {
                Row(Modifier.padding(horizontal = ScreenPad), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard(state.todayCount.toString(), "Hari ini", Modifier.weight(1f))
                    StatCard(formatNumber(state.todayLiters, 1) + " L", "Volume hari ini", Modifier.weight(1f))
                    StatCard(formatRupiahCompact(state.todayValue), "Nilai hari ini", Modifier.weight(1f))
                }
            }
            when {
                state.loading && state.transactions.isEmpty() -> item { LoadingBlock() }
                state.error != null && state.transactions.isEmpty() -> item { ErrorBlock(state.error, vm::refresh) }
                state.transactions.isEmpty() -> item {
                    EmptyBlock(BioIcons.Receipt, "Belum ada transaksi", "Tekan tombol + untuk mencatat penjualan dari Klien.")
                }
                else -> items(state.transactions) { tx ->
                    val name = tx.counterpartName(Viewer.Agen)
                    TxRow(
                        avatar = initialsOf(name),
                        title = name,
                        subtitle = formatRelativeDateTime(tx.createdAt) + " · " + formatLiter(tx.volumeLiter),
                        amount = formatRupiah(tx.totalPrice),
                        statusText = tx.txStatus.label(Viewer.Agen),
                        statusKind = tx.txStatus.chipKind(),
                        modifier = Modifier.padding(horizontal = ScreenPad),
                        onClick = { selectedId = tx.transactionId },
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(BioColors.Primary, RoundedCornerShape(18.dp))
                .clickable(onClick = onNewTransaction)
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(BioIcons.Plus, contentDescription = null, tint = BioColors.OnPrimary, modifier = Modifier.size(18.dp))
            Text("Transaksi", style = BioTheme.type.button, color = BioColors.OnPrimary)
        }
    }

    val selected = state.transactions.firstOrNull { it.transactionId == selectedId }
    if (selected != null) {
        AgenTxDetailSheet(selected) { selectedId = null }
    }
}

@Composable
private fun AgenTxDetailSheet(tx: TransactionDto, onDismiss: () -> Unit) {
    BioSheet("Detail transaksi", onDismiss) {
        DetailRow("ID Transaksi", tx.transactionId, mono = true)
        DetailRow("Tanggal", formatDateTime(tx.createdAt))
        DetailRow("Klien", tx.counterpartName(Viewer.Agen))
        DetailRow("ID Klien", tx.klienId, mono = true)
        DetailRow("Volume", formatLiter(tx.volumeLiter))
        DetailRow("Harga / liter", formatRupiah(tx.price))
        DetailRow("Total", formatRupiah(tx.totalPrice), mono = true, valueColor = BioColors.Primary)
        DetailRow(
            "Status", tx.txStatus.label(Viewer.Agen), last = true,
            valueColor = when (tx.txStatus) {
                TxStatus.Cancelled -> BioColors.Rust
                TxStatus.Pending -> BioColors.AmberDeep
                else -> BioColors.Primary
            },
        )
        Spacer(Modifier.height(14.dp))
        when (tx.txStatus) {
            TxStatus.Pending -> NoteBox(
                "Menunggu Klien menerima atau membatalkan di app-nya. Stok Anda bertambah setelah Klien menerima.",
                tone = NoteTone.Amber,
            )
            TxStatus.Cancelled -> NoteBox("Klien membatalkan transaksi ini. Stok Anda tidak berubah.", tone = NoteTone.Rust, icon = BioIcons.Alert)
            else -> Unit
        }
    }
}

@Composable
fun NewTransactionSheet(
    price: Long,
    creating: Boolean,
    onSubmit: (klienId: String, klienName: String, volumeLiter: Double) -> Unit,
    onDismiss: () -> Unit,
) {
    var klienId by remember { mutableStateOf("") }
    var klienName by remember { mutableStateOf("") }
    var volumeText by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf(emptyMap<String, String>()) }

    val volume = volumeText.trim().replace(',', '.').toDoubleOrNull()
    val total = if (volume != null && volume > 0) kotlin.math.round(volume * price).toLong() else 0L

    fun submit() {
        val found = buildMap {
            if (klienId.isBlank()) put("klienId", "ID Klien wajib diisi.")
            if (klienName.isBlank()) put("klienName", "Nama Klien wajib diisi.")
            if (volume == null || volume <= 0) put("volume", "Masukkan volume lebih dari 0.")
            else if (volume > 1000) put("volume", "Volume terlalu besar — periksa kembali.")
        }
        errors = found
        if (found.isEmpty() && volume != null) onSubmit(klienId.trim(), klienName.trim(), volume)
    }

    BioSheet("Input Transaksi Baru", onDismiss) {
        NoteBox(
            "Minta ID Klien dari menu \"ID Saya\" di app Klien. Setelah dikirim, Klien akan diminta menerima atau membatalkan.",
            icon = BioIcons.IdCard,
            modifier = Modifier.padding(bottom = 16.dp),
        )
        BioField("ID Klien", klienId, { klienId = it }, placeholder = "ID dari app Klien", error = errors["klienId"])
        BioField("Nama Klien", klienName, { klienName = it }, placeholder = "Nama Klien", error = errors["klienName"])
        BioField(
            "Volume minyak (liter)", volumeText, { volumeText = it },
            placeholder = "mis. 12,5",
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done,
            onDone = { submit() },
            error = errors["volume"],
        )
        BioField("Harga per liter", formatRupiah(price), {}, readOnly = true, hint = "Harga acuan Kilang")

        Row(
            Modifier.fillMaxWidth().background(BioColors.PrimaryTint, RoundedCornerShape(14.dp)).padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Total", style = BioTheme.type.bodyBold, color = BioColors.Primary)
            Text(formatRupiah(total), style = BioTheme.type.monoLarge, color = BioColors.Primary)
        }
        Spacer(Modifier.height(16.dp))
        BioButton("Kirim ke Klien", onClick = { submit() }, loading = creating, modifier = Modifier.fillMaxWidth())
    }
}

