package id.biojelan.app.ui.klien

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import id.biojelan.app.core.agenInitials
import id.biojelan.app.core.formatLiter
import id.biojelan.app.core.formatRupiah
import id.biojelan.app.core.greeting
import id.biojelan.app.data.remote.AgenSummaryDto
import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.ui.Viewer
import id.biojelan.app.ui.components.BioButton
import id.biojelan.app.ui.components.BioChip
import id.biojelan.app.ui.components.AvatarBox
import id.biojelan.app.ui.components.BtnStyle
import id.biojelan.app.ui.components.ChipKind
import id.biojelan.app.ui.components.CircleIconButton
import id.biojelan.app.ui.components.ErrorBlock
import id.biojelan.app.ui.components.LoadingBlock
import id.biojelan.app.ui.components.NoteBox
import id.biojelan.app.ui.components.NoteTone
import id.biojelan.app.ui.components.PriceBand
import id.biojelan.app.ui.components.ScreenPad
import id.biojelan.app.ui.components.SectionHead
import id.biojelan.app.ui.components.bioCard
import id.biojelan.app.ui.counterpartName
import id.biojelan.app.ui.icons.BioIcons
import id.biojelan.app.ui.theme.BioColors
import id.biojelan.app.ui.theme.BioTheme

@Composable
fun KlienHomeTab(
    state: KlienUiState,
    name: String,
    vm: KlienViewModel,
    onOpenAgen: (String) -> Unit,
    onGoTo: (Int) -> Unit,
    onShowId: () -> Unit,
) {
    val firstName = name.trim().substringBefore(' ').ifBlank { "Sobat BioJelan" }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = ScreenPad).padding(bottom = 24.dp),
    ) {
        Row(Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(greeting() + ",", style = BioTheme.type.body, color = BioColors.Muted)
                Text(firstName, style = BioTheme.type.headline, color = BioColors.Ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            CircleIconButton(BioIcons.Refresh, onClick = vm::refreshAll, contentDescription = "Muat ulang")
        }

        PriceBand(state.price, "Harga acuan Kilang · berlaku untuk semua Agen")

        state.pendingTx?.let { tx ->
            Spacer(Modifier.height(16.dp))
            PendingTxCard(
                tx = tx,
                agenName = tx.counterpartName(Viewer.Klien) { id -> state.agens.firstOrNull { it.agenId == id }?.name },
                busy = state.busyTxId == tx.transactionId,
                onAccept = { vm.accept(tx.transactionId) },
                onCancel = { vm.cancel(tx.transactionId) },
            )
        }

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickAction(BioIcons.Pin, "Cari Agen", Modifier.weight(1f)) { onGoTo(1) }
            QuickAction(BioIcons.Receipt, "Riwayat", Modifier.weight(1f)) { onGoTo(2) }
            QuickAction(BioIcons.IdCard, "ID Saya", Modifier.weight(1f), onClick = onShowId)
        }

        SectionHead("Agen BioJelan", action = "Lihat semua", onAction = { onGoTo(1) })
        when {
            state.agensLoading && state.agens.isEmpty() -> LoadingBlock()
            state.agensError != null && state.agens.isEmpty() -> ErrorBlock(state.agensError, onRetry = vm::loadAgens)
            state.agens.isEmpty() -> Text("Belum ada Agen terdaftar.", style = BioTheme.type.body, color = BioColors.Muted)
            else -> Row(
                Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                state.agens.take(8).forEach { agen -> AgenMiniCard(agen) { onOpenAgen(agen.agenId) } }
            }
        }

        Spacer(Modifier.height(20.dp))
        NoteBox(
            "Jual minyak jelantah Anda ke Agen. Agen mencatat transaksi, lalu Anda konfirmasi di sini — pembayaran diselesaikan langsung dengan Agen.",
            icon = BioIcons.Info,
        )
    }
}

@Composable
private fun QuickAction(icon: ImageVector, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.bioCard(16.dp).clickable(onClick = onClick).padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.size(38.dp).background(BioColors.PrimaryTint, CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = BioColors.Primary, modifier = Modifier.size(19.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(label, style = BioTheme.type.label, color = BioColors.Ink, textAlign = TextAlign.Center)
    }
}

@Composable
private fun AgenMiniCard(agen: AgenSummaryDto, onClick: () -> Unit) {
    Column(
        Modifier.width(200.dp).bioCard(16.dp).clickable(onClick = onClick).padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AvatarBox(agenInitials(agen.name), size = 40.dp)
            Column(Modifier.weight(1f)) {
                Text(agen.name, style = BioTheme.type.cardTitle, color = BioColors.Ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(agen.address, style = BioTheme.type.small, color = BioColors.Muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        Spacer(Modifier.height(10.dp))
        BioChip(if (agen.isOpen) "Buka" else "Tutup", if (agen.isOpen) ChipKind.Open else ChipKind.Closed)
    }
}

/** Kartu konfirmasi: Agen sudah mencatat transaksi, Klien menerima atau membatalkan. */
@Composable
fun PendingTxCard(
    tx: TransactionDto,
    agenName: String,
    busy: Boolean,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxWidth().bioCard(20.dp, background = BioColors.AmberTint, border = BioColors.Amber.copy(alpha = 0.5f)).padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(BioIcons.Bell, contentDescription = null, tint = BioColors.AmberDeep, modifier = Modifier.size(18.dp))
            Text("Konfirmasi transaksi", style = BioTheme.type.sectionTitle, color = BioColors.AmberText)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "$agenName mencatat penjualan ${formatLiter(tx.volumeLiter)} minyak jelantah atas nama Anda.",
            style = BioTheme.type.body,
            color = BioColors.AmberText,
        )
        Spacer(Modifier.height(4.dp))
        Text(formatRupiah(tx.totalPrice), style = BioTheme.type.monoLarge, color = BioColors.AmberText)
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BioButton("Batalkan", onCancel, Modifier.weight(1f), style = BtnStyle.Rust, enabled = !busy)
            BioButton("Terima", onAccept, Modifier.weight(1f), loading = busy, icon = BioIcons.Check)
        }
    }
}
