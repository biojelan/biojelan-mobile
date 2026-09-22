package id.biojelan.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.biojelan.app.ui.icons.BioIcons
import id.biojelan.app.ui.theme.BioColors
import id.biojelan.app.ui.theme.BioTheme

@Composable
fun BioField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false,
    readOnly: Boolean = false,
    hint: String? = null,
    error: String? = null,
    onDone: (() -> Unit)? = null,
) {
    var focused by remember { mutableStateOf(false) }
    var revealed by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)
    val borderColor = when {
        error != null -> BioColors.Rust
        focused -> BioColors.Primary
        else -> BioColors.Line
    }

    Column(modifier.fillMaxWidth().padding(bottom = 14.dp)) {
        Text(label, style = BioTheme.type.label, color = BioColors.InkSoft, modifier = Modifier.padding(bottom = 6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (readOnly) BioColors.PrimaryTint else BioColors.Surface, shape)
                .border(1.5.dp, borderColor, shape)
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = readOnly,
                singleLine = true,
                textStyle = BioTheme.type.body.copy(
                    color = if (readOnly) BioColors.Primary else BioColors.Ink,
                    fontSize = 14.5.sp,
                ),
                cursorBrush = SolidColor(BioColors.Primary),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
                keyboardActions = KeyboardActions(onDone = { onDone?.invoke() }),
                visualTransformation = if (isPassword && !revealed) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.weight(1f).onFocusChanged { focused = it.isFocused },
                decorationBox = { inner ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty() && placeholder.isNotEmpty()) {
                            Text(placeholder, style = BioTheme.type.body.copy(fontSize = 14.5.sp), color = BioColors.Muted)
                        }
                        inner()
                    }
                },
            )
            if (isPassword) {
                Icon(
                    imageVector = if (revealed) BioIcons.EyeOff else BioIcons.Eye,
                    contentDescription = if (revealed) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                    tint = BioColors.Muted,
                    modifier = Modifier.padding(start = 8.dp).size(20.dp).clip(RoundedCornerShape(6.dp)).clickable { revealed = !revealed },
                )
            }
        }
        val footer = error ?: hint
        if (footer != null) {
            Text(
                footer,
                style = BioTheme.type.small,
                color = if (error != null) BioColors.Rust else BioColors.Muted,
                modifier = Modifier.padding(top = 5.dp),
            )
        }
    }
}

/** Segmented control 2+ opsi (Masuk | Daftar, Semua | Buka sekarang). */
@Composable
fun SegmentedTabs(options: List<String>, selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().background(BioColors.Bg, RoundedCornerShape(14.dp)).padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEachIndexed { index, text ->
            val on = index == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (on) BioColors.Surface else BioColors.Bg, RoundedCornerShape(11.dp))
                    .clickable { onSelect(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(text, style = BioTheme.type.bodyBold, color = if (on) BioColors.Primary else BioColors.Muted)
            }
        }
    }
}

/** Pill filter kecil. */
@Composable
fun FilterPill(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(50)
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (selected) BioColors.Primary else BioColors.Surface, shape)
            .border(1.dp, if (selected) BioColors.Primary else BioColors.Line, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(text, style = BioTheme.type.label, color = if (selected) BioColors.OnPrimary else BioColors.InkSoft)
    }
}

/** Kolom pencarian dengan ikon. */
@Composable
fun SearchField(value: String, onValueChange: (String) -> Unit, placeholder: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier.fillMaxWidth().background(BioColors.Surface, shape).border(1.dp, BioColors.Line, shape).padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(BioIcons.Search, contentDescription = null, tint = BioColors.Muted, modifier = Modifier.size(18.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = BioTheme.type.body.copy(color = BioColors.Ink),
            cursorBrush = SolidColor(BioColors.Primary),
            modifier = Modifier.weight(1f),
            decorationBox = { inner ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) Text(placeholder, style = BioTheme.type.body, color = BioColors.Muted)
                    inner()
                }
            },
        )
    }
}
