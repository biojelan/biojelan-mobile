package id.biojelan.app.ui.account

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.biojelan.app.core.AppConfig
import id.biojelan.app.core.formatLiter
import id.biojelan.app.core.formatOperatingHours
import id.biojelan.app.core.initialsOf
import id.biojelan.app.data.remote.UserDto
import id.biojelan.app.data.repository.AgenEdit
import id.biojelan.app.ui.components.BioButton
import id.biojelan.app.ui.components.BioChip
import id.biojelan.app.ui.components.BioField
import id.biojelan.app.ui.components.BioSheet
import id.biojelan.app.ui.components.BtnStyle
import id.biojelan.app.ui.components.ChipKind
import id.biojelan.app.ui.components.FilterPill
import id.biojelan.app.ui.components.HairLine
import id.biojelan.app.ui.components.InfoItem
import id.biojelan.app.ui.components.NoteBox
import id.biojelan.app.ui.components.NoteTone
import id.biojelan.app.ui.components.ProfileRow
import id.biojelan.app.ui.components.ScreenPad
import id.biojelan.app.ui.components.ScreenTopBar
import id.biojelan.app.ui.components.SectionHead
import id.biojelan.app.ui.components.bioCard
import id.biojelan.app.ui.icons.BioIcons
import id.biojelan.app.ui.theme.BioColors
import id.biojelan.app.ui.theme.BioTheme

private enum class ProfileSheet { None, Edit, Password, Delete }

/** Tab Profil untuk Klien maupun Agen (bagian Agen muncul jika `user.agen != null`). */
@Composable
fun ProfileTab(user: UserDto, account: AccountViewModel) {
    var sheet by remember { mutableStateOf(ProfileSheet.None) }
    val busy by account.busy.collectAsStateWithLifecycle()
    val agen = user.agen
    val roleLabel = if (agen != null) "Agen" else "Klien"

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        ScreenTopBar("Profil")
        Column(Modifier.padding(horizontal = ScreenPad)) {

        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(76.dp).background(BioColors.Primary, CircleShape), contentAlignment = Alignment.Center) {
                Text(initialsOf(user.name), style = BioTheme.type.headline, color = BioColors.OnPrimary)
            }
            Spacer(Modifier.height(12.dp))
            Text(user.name, style = BioTheme.type.subTitle, color = BioColors.Ink, textAlign = TextAlign.Center)
            Spacer(Modifier.height(6.dp))
            BioChip(roleLabel, ChipKind.Open)
        }

        SectionHead("Data akun")
        Column(Modifier.fillMaxWidth().bioCard(16.dp)) {
            InfoItem(BioIcons.Mail, "Email", user.email.ifBlank { "-" })
            HairLine()
            InfoItem(BioIcons.Phone, "Telepon", user.phone.ifBlank { "-" })
            HairLine()
            InfoItem(BioIcons.IdCard, if (agen != null) "ID Agen" else "ID Klien", if (agen != null) agen.agenId.ifBlank { user.userId } else user.userId)
        }

        if (agen != null) {
            SectionHead("Data Agen")
            Column(Modifier.fillMaxWidth().bioCard(16.dp)) {
                InfoItem(BioIcons.Pin, "Alamat", agen.address.ifBlank { "-" })
                HairLine()
                InfoItem(BioIcons.Clock, "Jam operasional", formatOperatingHours(agen.openAt, agen.closeAt, agen.openDay))
                HairLine()
                InfoItem(
                    BioIcons.Bank, "Rekening",
                    if (agen.bankName.isBlank() && agen.accountNumber.isBlank()) "Belum diisi" else "${agen.bankName} · ${agen.accountNumber}",
                )
                HairLine()
                InfoItem(BioIcons.Drop, "Ambang stok", formatLiter(AppConfig.STOCK_THRESHOLD_LITER) + " · ditentukan Kilang")
            }
        }

        SectionHead("Pengaturan")
        Column(Modifier.fillMaxWidth().bioCard(16.dp)) {
            ProfileRow(BioIcons.Edit, "Ubah profil", onClick = { account.clearFormError(); sheet = ProfileSheet.Edit })
            HairLine()
            ProfileRow(BioIcons.Lock, "Ubah kata sandi", onClick = { account.clearFormError(); sheet = ProfileSheet.Password })
            HairLine()
            ProfileRow(BioIcons.Logout, "Keluar", tint = BioColors.Rust, onClick = { if (!busy) account.logout() })
        }

        Text(
            "Hapus akun",
            style = BioTheme.type.label,
            color = BioColors.Rust,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { account.clearFormError(); sheet = ProfileSheet.Delete }
                .padding(12.dp),
        )
        }
    }

    when (sheet) {
        ProfileSheet.Edit -> EditProfileSheet(user, account) { sheet = ProfileSheet.None }
        ProfileSheet.Password -> ChangePasswordSheet(account) { sheet = ProfileSheet.None }
        ProfileSheet.Delete -> DeleteAccountSheet(account) { sheet = ProfileSheet.None }
        ProfileSheet.None -> Unit
    }
}

private val TIME_REGEX = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")

@Composable
private fun EditProfileSheet(user: UserDto, account: AccountViewModel, onClose: () -> Unit) {
    val busy by account.busy.collectAsStateWithLifecycle()
    val serverError by account.formError.collectAsStateWithLifecycle()
    val agen = user.agen

    var name by remember { mutableStateOf(user.name) }
    var phone by remember { mutableStateOf(user.phone) }
    var address by remember { mutableStateOf(agen?.address.orEmpty()) }
    var bank by remember { mutableStateOf(agen?.bankName.orEmpty()) }
    var accountNumber by remember { mutableStateOf(agen?.accountNumber.orEmpty()) }
    var openAt by remember { mutableStateOf(agen?.openAt.orEmpty()) }
    var closeAt by remember { mutableStateOf(agen?.closeAt.orEmpty()) }
    var days by remember { mutableStateOf(agen?.openDay?.map { it.lowercase() }.orEmpty()) }
    var errors by remember { mutableStateOf(emptyMap<String, String>()) }

    BioSheet("Ubah profil", onDismiss = onClose) {
        BioField("Nama", name, { name = it }, error = errors["name"])
        BioField("Nomor telepon", phone, { phone = it }, keyboardType = KeyboardType.Phone, placeholder = "08xxxxxxxxxx")

        if (agen != null) {
            BioField("Alamat", address, { address = it }, placeholder = "Alamat lengkap")
            BioField("Nama bank", bank, { bank = it }, placeholder = "mis. BCA")
            BioField("Nomor rekening", accountNumber, { accountNumber = it }, keyboardType = KeyboardType.Number)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BioField("Buka", openAt, { openAt = it }, Modifier.weight(1f), placeholder = "08:00", error = errors["openAt"], keyboardType = KeyboardType.Number)
                BioField("Tutup", closeAt, { closeAt = it }, Modifier.weight(1f), placeholder = "17:00", error = errors["closeAt"], keyboardType = KeyboardType.Number)
            }
            Text("Hari buka", style = BioTheme.type.label, color = BioColors.InkSoft, modifier = Modifier.padding(bottom = 6.dp))
            Row(
                Modifier.horizontalScroll(rememberScrollState()).padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AppConfig.WEEK_DAYS.forEach { day ->
                    FilterPill(
                        text = day.replaceFirstChar { it.uppercase() },
                        selected = day in days,
                        onClick = { days = if (day in days) days - day else days + day },
                    )
                }
            }
        }

        serverError?.let { NoteBox(it, tone = NoteTone.Rust, icon = BioIcons.Alert, modifier = Modifier.padding(bottom = 12.dp)) }

        BioButton(
            "Simpan perubahan",
            onClick = {
                val found = buildMap {
                    if (name.isBlank()) put("name", "Nama wajib diisi.")
                    if (agen != null) {
                        if (openAt.isNotBlank() && !TIME_REGEX.matches(openAt.trim())) put("openAt", "Format HH:mm")
                        if (closeAt.isNotBlank() && !TIME_REGEX.matches(closeAt.trim())) put("closeAt", "Format HH:mm")
                    }
                }
                errors = found
                if (found.isEmpty()) {
                    val edit = if (agen != null) AgenEdit(
                        address = address.trim(),
                        bankName = bank.trim(),
                        accountNumber = accountNumber.trim(),
                        openAt = openAt.trim(),
                        closeAt = closeAt.trim(),
                        openDays = AppConfig.WEEK_DAYS.filter { it in days },
                    ) else null
                    account.updateProfile(name, phone, edit, onSuccess = onClose)
                }
            },
            loading = busy,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ChangePasswordSheet(account: AccountViewModel, onClose: () -> Unit) {
    val busy by account.busy.collectAsStateWithLifecycle()
    val serverError by account.formError.collectAsStateWithLifecycle()
    var current by remember { mutableStateOf("") }
    var next by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf(emptyMap<String, String>()) }

    fun submit() {
        val found = buildMap {
            if (current.isEmpty()) put("current", "Kata sandi saat ini wajib diisi.")
            if (next.length < 8) put("next", "Minimal 8 karakter.")
            if (confirm != next) put("confirm", "Konfirmasi tidak cocok.")
        }
        errors = found
        if (found.isEmpty()) account.changePassword(current, next, confirm, onSuccess = onClose)
    }

    BioSheet("Ubah kata sandi", onDismiss = onClose) {
        BioField("Kata sandi saat ini", current, { current = it }, isPassword = true, keyboardType = KeyboardType.Password, error = errors["current"])
        BioField("Kata sandi baru", next, { next = it }, isPassword = true, keyboardType = KeyboardType.Password, hint = "Minimal 8 karakter", error = errors["next"])
        BioField(
            "Ulangi kata sandi baru", confirm, { confirm = it },
            isPassword = true, keyboardType = KeyboardType.Password, imeAction = ImeAction.Done,
            onDone = { submit() }, error = errors["confirm"],
        )
        serverError?.let { NoteBox(it, tone = NoteTone.Rust, icon = BioIcons.Alert, modifier = Modifier.padding(bottom = 12.dp)) }
        BioButton("Simpan kata sandi", onClick = { submit() }, loading = busy, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun DeleteAccountSheet(account: AccountViewModel, onClose: () -> Unit) {
    val busy by account.busy.collectAsStateWithLifecycle()
    val serverError by account.formError.collectAsStateWithLifecycle()
    var typed by remember { mutableStateOf("") }

    BioSheet("Hapus akun", onDismiss = onClose) {
        NoteBox(
            "Akun beserta datanya akan dihapus permanen dan tidak bisa dipulihkan.",
            tone = NoteTone.Rust,
            icon = BioIcons.Alert,
            modifier = Modifier.padding(bottom = 16.dp),
        )
        BioField("Ketik HAPUS untuk melanjutkan", typed, { typed = it }, placeholder = "HAPUS")
        serverError?.let { NoteBox(it, tone = NoteTone.Rust, icon = BioIcons.Alert, modifier = Modifier.padding(bottom = 12.dp)) }
        BioButton(
            "Hapus akun saya",
            onClick = { account.deleteAccount() },
            style = BtnStyle.Rust,
            enabled = typed.trim() == "HAPUS",
            loading = busy,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
