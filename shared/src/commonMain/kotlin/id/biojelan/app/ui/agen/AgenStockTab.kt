package id.biojelan.app.ui.agen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.biojelan.app.core.AppConfig
import id.biojelan.app.core.formatLiter
import id.biojelan.app.core.formatNumber
import id.biojelan.app.core.formatStockTime
import id.biojelan.app.data.remote.UserDto
import id.biojelan.app.data.repository.TxStatus
import id.biojelan.app.data.repository.txStatus
import id.biojelan.app.ui.Viewer
import id.biojelan.app.ui.components.BioChip
import id.biojelan.app.ui.components.ChipKind
import id.biojelan.app.ui.components.CircleIconButton
import id.biojelan.app.ui.components.DropGauge
import id.biojelan.app.ui.components.DropGaugeTone
import id.biojelan.app.ui.components.NoteBox
import id.biojelan.app.ui.components.ScreenPad
import id.biojelan.app.ui.components.ScreenTopBar
import id.biojelan.app.ui.components.SectionHead
import id.biojelan.app.ui.components.StockHistoryRow
import id.biojelan.app.ui.counterpartName
import id.biojelan.app.ui.icons.BioIcons
import id.biojelan.app.ui.strings.BioText
import id.biojelan.app.ui.theme.BioTheme

/**
 * Stok Agen — dibuat 1:1 dengan `#screen-agen-stok` di prototype: gauge besar, catatan ambang,
 * riwayat pergerakan stok ringkas (`.stock-hrow`, tanpa avatar/chip), dan link laporan selisih.
 * Status penjemputan Driver dipindah ke tab Pickup tersendiri ([AgenPickupTab]) — di prototype pun
 * itu layar terpisah, bukan bagian dari layar Stok.
 *
 * Nilai `stock_liter` datang dari GET /api/user (dihitung server). "Riwayat pergerakan stok"
 * diturunkan dari daftar transaksi karena API-DOC belum punya endpoint riwayat stok tersendiri.
 */
@Composable
fun AgenStockTab(state: AgenUiState, user: UserDto, vm: AgenViewModel) {
    val c = BioTheme.colors
    val s = BioText.current
    val stock = user.agen?.stockLiter ?: 0.0
    val threshold = AppConfig.STOCK_THRESHOLD_LITER
    val reached = stock >= threshold
    val ratio = (stock / threshold).toFloat().coerceIn(0f, 1f)

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        ScreenTopBar(s.stockTitle, actions = {
            CircleIconButton(BioIcons.Clock, onClick = vm::infoStockHistory, contentDescription = s.stockMovement)
        })
        Column(Modifier.padding(horizontal = ScreenPad), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(4.dp))
            DropGauge(
                fill = ratio,
                tone = DropGaugeTone.Primary,
                modifier = Modifier.size(width = 132.dp, height = 164.dp),
            )
            Spacer(Modifier.height(12.dp))
            Text(formatLiter(stock), style = BioTheme.type.title.copy(fontSize = 34.sp), color = c.ink)
            Text(s.thresholdCaption(formatLiter(threshold)), style = BioTheme.type.body, color = c.muted)
            Spacer(Modifier.height(10.dp))
            BioChip(
                if (reached) s.aboveThresholdNote else s.remainingToThreshold(formatLiter(threshold - stock)),
                if (reached) ChipKind.Done else ChipKind.Pending,
            )
        }

        Column(Modifier.padding(horizontal = ScreenPad)) {
            SectionHead(s.stockMovement)
            if (state.transactions.isEmpty()) {
                NoteBox(s.noStockMovement)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.transactions.take(20).forEach { tx ->
                        val name = tx.counterpartName(Viewer.Agen)
                        val cancelled = tx.txStatus == TxStatus.Cancelled
                        StockHistoryRow(
                            label = s.transactionDash(name),
                            value = if (cancelled) "0 L" else "+" + formatNumber(tx.volumeLiter, 2) + " L",
                            time = formatStockTime(tx.createdAt),
                            cancelled = cancelled,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(4.dp))
        Text(
            s.stockCorrectionLink,
            style = BioTheme.type.label,
            color = c.rust,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = ScreenPad, vertical = 4.dp)
                .fillMaxWidth()
                .background(Color.Transparent, RoundedCornerShape(14.dp))
                .border(1.5.dp, c.rustTint, RoundedCornerShape(14.dp))
                .clickable(onClick = vm::infoStockCorrection)
                .padding(12.dp),
        )
    }
}
