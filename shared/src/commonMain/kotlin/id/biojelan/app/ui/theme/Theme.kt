package id.biojelan.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import id.biojelan.app.resources.Res
import id.biojelan.app.resources.ibmplexmono_medium
import id.biojelan.app.resources.ibmplexmono_semibold
import id.biojelan.app.resources.manrope_bold
import id.biojelan.app.resources.manrope_extrabold
import id.biojelan.app.resources.manrope_medium
import id.biojelan.app.resources.manrope_regular
import id.biojelan.app.resources.manrope_semibold
import id.biojelan.app.resources.sora_bold
import id.biojelan.app.resources.sora_extrabold
import id.biojelan.app.resources.sora_semibold
import org.jetbrains.compose.resources.Font

/** Token warna — disalin dari `:root` di BioJelan-Prototype-Mobile.html. */
object BioColors {
    val Bg = Color(0xFFEEF2E8)
    val Paper = Color(0xFFF7F9F3)
    val Surface = Color(0xFFFFFFFF)
    val Ink = Color(0xFF16261F)
    val InkSoft = Color(0xFF3C4A41)
    val Muted = Color(0xFF6E7C71)
    val Line = Color(0xFFDCE3D5)
    val Primary = Color(0xFF1F4D3A)
    val PrimaryDeep = Color(0xFF143329)
    val PrimaryTint = Color(0xFFDCEADF)
    val Amber = Color(0xFFD69A2E)
    val AmberDeep = Color(0xFFA8701D)
    val AmberTint = Color(0xFFF7E9CB)
    val AmberText = Color(0xFF5C4419)
    val Rust = Color(0xFFB85C2A)
    val RustTint = Color(0xFFF3DFCF)
    val Steel = Color(0xFF3E6B85)
    val OnPrimary = Color(0xFFEFF6EF)
    val MapBase = Color(0xFFEEF2E4)
    val MapGrid = Color(0xFFE4EBDA)
}

/** Skala tipografi: Sora (judul & angka besar), Manrope (teks), IBM Plex Mono (nominal). */
@Immutable
class BioType(sora: FontFamily, manrope: FontFamily, mono: FontFamily) {
    val title = TextStyle(fontFamily = sora, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, letterSpacing = (-0.02).em)
    val headline = TextStyle(fontFamily = sora, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
    val topTitle = TextStyle(fontFamily = sora, fontWeight = FontWeight.Bold, fontSize = 19.sp, letterSpacing = (-0.01).em)
    val subTitle = TextStyle(fontFamily = sora, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
    val sectionTitle = TextStyle(fontFamily = sora, fontWeight = FontWeight.ExtraBold, fontSize = 15.5.sp, letterSpacing = (-0.01).em)
    val display = TextStyle(fontFamily = sora, fontWeight = FontWeight.ExtraBold, fontSize = 23.sp, letterSpacing = (-0.01).em)
    val statNumber = TextStyle(fontFamily = sora, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    val cardTitle = TextStyle(fontFamily = manrope, fontWeight = FontWeight.ExtraBold, fontSize = 13.5.sp)
    val body = TextStyle(fontFamily = manrope, fontWeight = FontWeight.Medium, fontSize = 13.5.sp, lineHeight = 20.sp)
    val bodyBold = TextStyle(fontFamily = manrope, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    val small = TextStyle(fontFamily = manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp, lineHeight = 17.sp)
    val label = TextStyle(fontFamily = manrope, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    val caption = TextStyle(fontFamily = manrope, fontWeight = FontWeight.Bold, fontSize = 10.5.sp)
    val eyebrow = TextStyle(fontFamily = manrope, fontWeight = FontWeight.ExtraBold, fontSize = 11.5.sp, letterSpacing = 0.06.em)
    val button = TextStyle(fontFamily = manrope, fontWeight = FontWeight.ExtraBold, fontSize = 14.5.sp)
    val chip = TextStyle(fontFamily = manrope, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
    val tab = TextStyle(fontFamily = manrope, fontWeight = FontWeight.Bold, fontSize = 10.sp)
    val mono = TextStyle(fontFamily = mono, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    val monoSmall = TextStyle(fontFamily = mono, fontWeight = FontWeight.Medium, fontSize = 12.sp)
    val monoLarge = TextStyle(fontFamily = mono, fontWeight = FontWeight.SemiBold, fontSize = 19.sp)
}

private val LocalBioType = staticCompositionLocalOf<BioType> { error("BioTheme belum dipasang") }

object BioTheme {
    val type: BioType
        @Composable @ReadOnlyComposable get() = LocalBioType.current
}

@Composable
fun BioTheme(content: @Composable () -> Unit) {
    val sora = FontFamily(
        Font(Res.font.sora_semibold, FontWeight.SemiBold),
        Font(Res.font.sora_bold, FontWeight.Bold),
        Font(Res.font.sora_extrabold, FontWeight.ExtraBold),
    )
    val manrope = FontFamily(
        Font(Res.font.manrope_regular, FontWeight.Normal),
        Font(Res.font.manrope_medium, FontWeight.Medium),
        Font(Res.font.manrope_semibold, FontWeight.SemiBold),
        Font(Res.font.manrope_bold, FontWeight.Bold),
        Font(Res.font.manrope_extrabold, FontWeight.ExtraBold),
    )
    val mono = FontFamily(
        Font(Res.font.ibmplexmono_medium, FontWeight.Medium),
        Font(Res.font.ibmplexmono_semibold, FontWeight.SemiBold),
    )
    val type = remember(sora, manrope, mono) { BioType(sora, manrope, mono) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = BioColors.Primary,
            onPrimary = BioColors.OnPrimary,
            secondary = BioColors.AmberDeep,
            background = BioColors.Paper,
            onBackground = BioColors.Ink,
            surface = BioColors.Paper,
            onSurface = BioColors.Ink,
            error = BioColors.Rust,
        ),
    ) {
        CompositionLocalProvider(LocalBioType provides type) {
            Surface(color = BioColors.Paper, contentColor = BioColors.Ink) {
                content()
            }
        }
    }
}
