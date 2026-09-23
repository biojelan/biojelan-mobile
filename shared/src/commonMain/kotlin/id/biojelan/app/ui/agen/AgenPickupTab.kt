package id.biojelan.app.ui.agen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.biojelan.app.core.formatDateOnly
import id.biojelan.app.core.formatRelativeDateTime
import id.biojelan.app.data.remote.PickupStatusDto
import id.biojelan.app.data.repository.PickupStatus
import id.biojelan.app.data.repository.pickupStatus
import id.biojelan.app.ui.components.BioChip
import id.biojelan.app.ui.components.ChipKind
import id.biojelan.app.ui.components.CircleIconButton
import id.biojelan.app.ui.components.InfoItem
import id.biojelan.app.ui.components.NoteBox
import id.biojelan.app.ui.components.NoteTone
import id.biojelan.app.ui.components.ScreenPad
import id.biojelan.app.ui.components.ScreenTopBar
import id.biojelan.app.ui.components.bioCard
import id.biojelan.app.ui.icons.BioIcons
import id.biojelan.app.ui.strings.BioText
import id.biojelan.app.ui.theme.BioTheme

/**
 * Info Pickup Agen — padanan `#screen-agen-pickup` di prototype.
 *
 * CATATAN PENTING soal kesetiaan ke prototype: mockup HTML menampilkan kartu Driver (nama, plat),
 * estimasi volume yang diambil, dan daftar Agen lain dalam satu rute. Semua itu TIDAK ada di
 * response nyata `GET /api/agen/pickup/status` (lihat API-DOC/pickup.md) — cuma `pickup_id`,
 * `status`, `updated_at`. Field `date` didapat dari status terakhir yang di-set Kilang, bukan dari
 * endpoint ini secara langsung. Daripada mengarang nama Driver/plat/estimasi volume yang tidak ada
 * datanya, layar ini hanya menampilkan apa yang benar-benar tersedia, dengan gaya visual (kartu +
 * stepper) semirip mungkin dengan prototype. Stepper juga disederhanakan jadi 3 langkah nyata
 * (Ditugaskan → Dalam perjalanan → Selesai) karena `PickupStatus` backend cuma punya
 * ASSIGNED/OTW/COMPLETED/CANCELLED — bukan 5 langkah (Waiting/Scheduled/Assigned/In Progress/
 * Completed) seperti di mockup.
 */
@Composable
fun AgenPickupTab(state: AgenUiState, vm: AgenViewModel) {
    val c = BioTheme.colors
    val s = BioText.current

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        ScreenTopBar(s.pickupTitle, actions = {
            CircleIconButton(BioIcons.Refresh, onClick = vm::refresh, contentDescription = s.reload)
        })
        Column(Modifier.padding(horizontal = ScreenPad)) {
            val pickup = state.pickup
            when {
                pickup == null && state.pickupLoading -> {
                    Row(
                        Modifier.fillMaxWidth().bioCard(16.dp).padding(24.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator(color = c.primary, strokeWidth = 3.dp, modifier = Modifier.size(22.dp))
                    }
                }
                pickup == null -> NoteBox(s.pickupNoneScheduled, icon = BioIcons.Truck)
                else -> PickupContent(pickup)
            }
        }
    }
}

@Composable
private fun PickupContent(pickup: PickupStatusDto) {
    val c = BioTheme.colors
    val s = BioText.current
    val status = pickup.pickupStatus

    val (chipText, chipKind) = when (status) {
        PickupStatus.Assigned -> s.pickupStatusAssigned to ChipKind.Pending
        PickupStatus.OnTheWay -> s.pickupStatusOtw to ChipKind.Pending
        PickupStatus.Completed -> s.pickupStatusCompleted to ChipKind.Done
        PickupStatus.Cancelled -> s.pickupStatusCancelled to ChipKind.Cancelled
        PickupStatus.Unknown -> pickup.status to ChipKind.Neutral
    }

    Column(Modifier.fillMaxWidth().bioCard(20.dp).padding(18.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(s.pickupIdLabel, style = BioTheme.type.caption, color = c.muted)
                Spacer(Modifier.height(2.dp))
                Text(pickup.pickupId.ifBlank { "-" }, style = BioTheme.type.mono, color = c.ink)
            }
            BioChip(chipText, chipKind)
        }
        if (status == PickupStatus.Assigned || status == PickupStatus.OnTheWay || status == PickupStatus.Completed) {
            Spacer(Modifier.height(20.dp))
            PickupStepper(status)
        }
    }

    if (pickup.date.isNotBlank()) {
        Spacer(Modifier.height(12.dp))
        Column(Modifier.fillMaxWidth().bioCard(14.dp)) {
            InfoItem(BioIcons.Calendar, s.pickupScheduleLabel, formatDateOnly(pickup.date))
        }
    }

    val note = when (status) {
        PickupStatus.Assigned -> s.pickupNoteAssigned
        PickupStatus.OnTheWay -> s.pickupNoteOtw
        PickupStatus.Completed -> s.pickupNoteCompleted
        else -> null
    }
    if (note != null) {
        Spacer(Modifier.height(12.dp))
        NoteBox(note, icon = BioIcons.Truck, tone = if (status == PickupStatus.Completed) NoteTone.Neutral else NoteTone.Amber)
    }

    Spacer(Modifier.height(12.dp))
    BioChip(s.pickupUpdatedAt(formatRelativeDateTime(pickup.updatedAt)), ChipKind.Neutral)
}

/** Stepper 3-langkah (Ditugaskan → Dalam perjalanan → Selesai), gaya visual ala `.stepper` prototype. */
@Composable
private fun PickupStepper(status: PickupStatus, modifier: Modifier = Modifier) {
    val c = BioTheme.colors
    val s = BioText.current
    val steps = listOf(PickupStatus.Assigned, PickupStatus.OnTheWay, PickupStatus.Completed)
    val labels = listOf(s.pickupStepAssigned, s.pickupStepOtw, s.pickupStepCompleted)
    val currentIndex = steps.indexOf(status).coerceAtLeast(0)
    val doneFraction = currentIndex.toFloat() / (steps.size - 1)

    Column(modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().height(26.dp)) {
            Box(
                Modifier.align(Alignment.Center).fillMaxWidth().padding(horizontal = 13.dp)
                    .height(2.dp).background(c.line),
            )
            Box(
                Modifier.align(Alignment.CenterStart).fillMaxWidth(doneFraction).padding(start = 13.dp)
                    .height(2.dp).background(c.primary),
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                steps.indices.forEach { i ->
                    val done = i < currentIndex
                    val now = i == currentIndex
                    val bg = when {
                        done -> c.primary
                        now -> c.amberDeep
                        else -> c.line
                    }
                    Box(Modifier.size(26.dp).background(bg, CircleShape), contentAlignment = Alignment.Center) {
                        if (done) {
                            Icon(BioIcons.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                        } else {
                            Text((i + 1).toString(), style = BioTheme.type.caption, color = if (now) Color.White else c.muted)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(Modifier.fillMaxWidth()) {
            labels.forEachIndexed { i, label ->
                Text(
                    label,
                    style = BioTheme.type.caption.copy(fontSize = 9.sp),
                    color = if (i <= currentIndex) c.ink else c.muted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
