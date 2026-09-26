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
`data/repository` (Auth/User/Transaction/Pickup/Price/Session), `ui/{auth,klien,agen,account,components,theme}`.

## Menjalankan

- **Android**: buka folder ini di Android Studio → jalankan konfigurasi `androidApp`.
- **iOS**: buka `iosApp/iosApp.xcodeproj` di Xcode (isi `TEAM_ID` di `iosApp/Configuration/Config.xcconfig` untuk device).
- Base URL ada di `shared/.../core/AppConfig.kt` (`BASE_URL`, saat ini `http://biojelan.id`).

## Cakupan (sesuai API-DOC)

| Peran | Layar |
|---|---|
| Semua | Masuk, Daftar, Lupa kata sandi, Profil (ubah profil, ubah kata sandi, keluar, hapus akun) |
| Klien | Beranda (harga, konfirmasi transaksi, agen), Cari Agen, Detail Agen, Riwayat + detail (Terima / Batalkan) |
| Agen | Beranda (buka/tutup, statistik, stok), Transaksi + input transaksi, Stok, Pickup (status penjemputan Driver) |
| Driver | **Satu app yang sama** dengan Klien/Agen (bukan app terpisah) — di prototype ada 3 tab (Rute, Riwayat, Profil), tapi **belum diimplementasi** di kode ini. Lihat catatan di bawah. |

Peran Klien/Agen ditentukan otomatis dari `GET /api/user`: ada objek `agen` → Agen, selain itu →
Klien. **Belum ada cara serupa buat mendeteksi Driver** — `user.md` belum punya field semacam
`"driver": {...}` atau nilai `role_id` yang beda buat Driver (di contoh doc, Klien dan Agen sama-sama
`role_id: 7`). Jadi sebelum layar Driver bisa dibangun di app ini, backend perlu nambahin cara buat
app tahu "user yang login ini Driver" lewat `GET /api/user`.

## Status penjemputan Driver (pickup.md)

Sisi Agen dari `pickup.md` sudah terhubung: `GET /api/agen/pickup/status` dibaca lewat
`PickupRepository`, ditampilkan di tab Pickup (dan sebagai chip ringkas di Beranda saat status
`ASSIGNED`/`OTW`). Sisi Kilang (`POST /api/kilang/pickup/status`, buat penugasan) tetap di luar
cakupan app ini — itu dari dashboard web Kilang. Sisi **Driver** (`PATCH`/`GET
/api/driver/pickup/status`, plus `POST /api/driver/transaction` & `GET /api/driver/transactions` di
`transaction_agen.md`) **ada dalam cakupan app mobile ini** (satu app buat Klien/Agen/Driver, sesuai
prototype) — cuma memang belum dibangun; lihat gap deteksi role di atas.

## Belum ada di API-DOC (sengaja tidak dibuat / memakai placeholder)

Upgrade Klien→Agen, mode tamu (`/api/user/agen` butuh token), OTP, ambang stok, riwayat stok
tersendiri (dipakai turunan dari daftar transaksi), koreksi stok, jarak/lokasi pengguna. Nilai
ambang stok sementara ada di `AppConfig.kt`.

## Catatan struktur proyek

Kalau paket ini diambil dari arsip lama: folder `shared/` sempat berisi salinan ganda seluruh
proyek (root ter-nested di dalam `shared/shared/`). Sudah dirapikan di sini — `shared/`
sekarang cuma berisi `build.gradle.kts` modul KMP + `src/`, seperti struktur normal.

## Font

Sora, Manrope, IBM Plex Mono (SIL OFL 1.1) — lisensi di `licenses/fonts/`.
