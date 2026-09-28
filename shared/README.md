# BioJelan Mobile (Kotlin Multiplatform)

Aplikasi mobile BioJelan — **Compose Multiplatform** (Android + iOS, satu basis kode UI) yang terhubung ke
backend **Next.js + MySQL** sesuai `API-DOC/`. Tampilan mengikuti `BioJelan-Prototype-Mobile.html`.

## Struktur

```
shared/      Modul KMP: seluruh UI, ViewModel, jaringan (Ktor), sesi
androidApp/  Host Android (MainActivity + Application)
iosApp/      Host iOS (SwiftUI membungkus ComposeUIViewController)
API-DOC/     Dokumentasi API (sumber kebenaran)
```

Paket utama `id.biojelan.app`: `core/` (config, format), `data/remote` (DTO + `ApiClient`),
`data/repository` (Auth/User/Transaction/Session), `ui/{auth,klien,agen,account,components,theme}`.

## Menjalankan

- **Android**: buka folder ini di Android Studio → jalankan konfigurasi `androidApp`.
- **iOS**: buka `iosApp/iosApp.xcodeproj` di Xcode (isi `TEAM_ID` di `iosApp/Configuration/Config.xcconfig` untuk device).
- Base URL ada di `shared/.../core/AppConfig.kt` (`BASE_URL`, saat ini `http://biojelan.id`).

## Cakupan (sesuai API-DOC)

| Peran | Layar |
|---|---|
| Semua | Masuk, Daftar, Lupa kata sandi, Profil (ubah profil, ubah kata sandi, keluar, hapus akun) |
| Klien | Beranda (harga, konfirmasi transaksi, agen), Cari Agen, Detail Agen, Riwayat + detail (Terima / Batalkan) |
| Agen | Beranda (buka/tutup, statistik, stok), Transaksi + input transaksi, Stok |

Peran ditentukan otomatis dari `GET /api/user`: ada objek `agen` → Agen, selain itu → Klien.

## Belum ada di API-DOC (sengaja tidak dibuat / memakai placeholder)

Driver & rute jemput, tab Pickup Agen, upgrade Klien→Agen, mode tamu (`/api/user/agen` butuh token), OTP,
harga jelantah, ambang stok, riwayat stok, koreksi stok, jarak/lokasi pengguna. Nilai harga & ambang
sementara ada di `AppConfig.kt`.

## Font

Sora, Manrope, IBM Plex Mono (SIL OFL 1.1) — lisensi di `licenses/fonts/`.
