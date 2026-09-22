package id.biojelan.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.biojelan.app.ui.icons.BioIcons
import id.biojelan.app.ui.theme.BioColors
import id.biojelan.app.ui.theme.BioTheme

/** Padding horizontal standar layar (22px di prototype). */
val ScreenPad = 22.dp

// ------------------------------------------------------------------ modifier

fun Modifier.bioCard(
    radius: Dp = 16.dp,
    background: Color = BioColors.Surface,
    border: Color = BioColors.Line,
): Modifier {
    val shape = RoundedCornerShape(radius)
    return this
        .background(background, shape)
        .border(1.dp, border, shape)
        .clip(shape)
}

// ------------------------------------------------------------------ tombol

enum class BtnStyle { Primary, Outline, Rust, Amber }

@Composable
fun BioButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: BtnStyle = BtnStyle.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
) {
    val active = enabled && !loading
    val (bg, fg, borderColor) = when (style) {
        BtnStyle.Primary -> Triple(BioColors.Primary, BioColors.OnPrimary, BioColors.Primary)
        BtnStyle.Outline -> Triple(BioColors.Surface, BioColors.Primary, BioColors.Line)
        BtnStyle.Rust -> Triple(BioColors.Surface, BioColors.Rust, BioColors.Rust)
        BtnStyle.Amber -> Triple(BioColors.Amber, BioColors.Ink, BioColors.Amber)
    }
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (active || style != BtnStyle.Primary) bg else BioColors.Line, shape)
            .border(BorderStroke(1.5.dp, if (active || style != BtnStyle.Primary) borderColor else BioColors.Line), shape)
            .clickable(enabled = active, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        val contentColor = if (active) fg else if (style == BtnStyle.Primary) BioColors.Muted else fg.copy(alpha = 0.45f)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = contentColor, strokeWidth = 2.dp)
            } else if (icon != null) {
                Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(17.dp))
            }
            Text(text, style = BioTheme.type.button, color = contentColor, maxLines = 1)
        }
    }
}

@Composable
fun CircleIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = BioColors.Primary,
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .background(BioColors.Surface, CircleShape)
            .border(1.dp, BioColors.Line, CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription, tint = tint, modifier = Modifier.size(18.dp))
    }
}

// ------------------------------------------------------------------ chip & drip

enum class ChipKind { Open, Closed, Pending, Done, Cancelled, Neutral }

@Composable
fun BioChip(text: String, kind: ChipKind, modifier: Modifier = Modifier) {
    val (bg, fg) = when (kind) {
        ChipKind.Open, ChipKind.Done -> BioColors.PrimaryTint to BioColors.Primary
        ChipKind.Closed, ChipKind.Cancelled -> BioColors.RustTint to BioColors.Rust
        ChipKind.Pending -> BioColors.AmberTint to BioColors.AmberText
        ChipKind.Neutral -> BioColors.Line to BioColors.InkSoft
    }
    Box(
        modifier = modifier.background(bg, RoundedCornerShape(50)).padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(text, style = BioTheme.type.chip, color = fg, maxLines = 1)
    }
}

private val DripShape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50, bottomEndPercent = 50, bottomStartPercent = 0)

/** Titik status berbentuk tetesan (ala prototype). */
@Composable
fun Drip(color: Color, modifier: Modifier = Modifier, size: Dp = 8.dp) {
    Box(modifier = modifier.size(size).rotate(45f).background(color, DripShape))
}

// ------------------------------------------------------------------ header

@Composable
fun ScreenTopBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(start = ScreenPad, end = ScreenPad, top = 12.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = BioTheme.type.topTitle, color = BioColors.Ink, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        actions()
    }
}

@Composable
fun SubHeader(title: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(start = 16.dp, end = ScreenPad, top = 10.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        CircleIconButton(BioIcons.Back, onBack, contentDescription = "Kembali")
        Text(title, style = BioTheme.type.subTitle, color = BioColors.Ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun SectionHead(title: String, modifier: Modifier = Modifier, action: String? = null, onAction: (() -> Unit)? = null) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 22.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = BioTheme.type.sectionTitle, color = BioColors.Ink, modifier = Modifier.weight(1f))
        if (action != null && onAction != null) {
            Text(
                action,
                style = BioTheme.type.label,
                color = BioColors.Primary,
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(onClick = onAction).padding(4.dp),
            )
        }
    }
}

// ------------------------------------------------------------------ kartu kecil

@Composable
fun StatCard(number: String, label: String, modifier: Modifier = Modifier, mono: Boolean = false) {
    Column(
        modifier = modifier.bioCard(14.dp).padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            number,
            style = if (mono) BioTheme.type.mono.copy(fontSize = 15.sp) else BioTheme.type.statNumber,
            color = BioColors.Primary,
            maxLines = 1,
        )
        Spacer(Modifier.height(2.dp))
        Text(label, style = BioTheme.type.caption, color = BioColors.Muted, maxLines = 2)
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = BioColors.Ink,
    mono: Boolean = false,
    last: Boolean = false,
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 11.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(label, style = BioTheme.type.body, color = BioColors.Muted, modifier = Modifier.width(110.dp))
            Text(
                value,
                style = if (mono) BioTheme.type.mono else BioTheme.type.bodyBold,
                color = valueColor,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f),
            )
        }
        if (!last) Box(Modifier.fillMaxWidth().height(1.dp).background(BioColors.Line))
    }
}

enum class NoteTone { Neutral, Amber, Rust }

@Composable
fun NoteBox(text: String, modifier: Modifier = Modifier, tone: NoteTone = NoteTone.Neutral, icon: ImageVector = BioIcons.Info) {
    val (bg, fg) = when (tone) {
        NoteTone.Neutral -> BioColors.PrimaryTint to BioColors.Primary
        NoteTone.Amber -> BioColors.AmberTint to BioColors.AmberText
        NoteTone.Rust -> BioColors.RustTint to BioColors.Rust
    }
    Row(
        modifier = modifier.fillMaxWidth().background(bg, RoundedCornerShape(14.dp)).padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(18.dp))
        Text(text, style = BioTheme.type.small, color = fg, modifier = Modifier.weight(1f))
    }
}

@Composable
fun AvatarBox(text: String, modifier: Modifier = Modifier, size: Dp = 44.dp, shape: Shape = RoundedCornerShape(14.dp), tone: NoteTone = NoteTone.Neutral) {
    val (bg, fg) = when (tone) {
        NoteTone.Neutral -> BioColors.PrimaryTint to BioColors.Primary
        NoteTone.Amber -> BioColors.AmberTint to BioColors.AmberDeep
        NoteTone.Rust -> BioColors.RustTint to BioColors.Rust
    }
    Box(modifier.size(size).background(bg, shape), contentAlignment = Alignment.Center) {
        Text(text, style = BioTheme.type.cardTitle, color = fg)
    }
}

/** Baris ikon + label kecil + nilai (dipakai di kartu detail Agen & profil). */
@Composable
fun InfoItem(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Row(
        modifier = modifier.fillMaxWidth()
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(34.dp).background(BioColors.PrimaryTint, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = BioColors.Primary, modifier = Modifier.size(17.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(label, style = BioTheme.type.caption, color = BioColors.Muted)
            Text(value, style = BioTheme.type.bodyBold, color = BioColors.Ink)
        }
        if (onClick != null) Icon(BioIcons.Chevron, contentDescription = null, tint = BioColors.Muted, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun HairLine(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().height(1.dp).background(BioColors.Line))
}

fun Modifier.gradientBackground(colors: List<Color>, shape: Shape): Modifier =
    this.background(Brush.linearGradient(colors), shape)
