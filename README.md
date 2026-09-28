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
| Agen | Beranda (buka/tutup, statistik, stok), Transaksi + input transaksi, Stok (+ status penjemputan Driver) |

Peran ditentukan otomatis dari `GET /api/user`: ada objek `agen` → Agen, selain itu → Klien.

## Status penjemputan Driver (pickup.md)

Sisi Agen dari `pickup.md` sudah terhubung: `GET /api/agen/pickup/status` dibaca lewat
`PickupRepository`, ditampilkan sebagai kartu di tab Stok (dan sebagai chip ringkas di Beranda saat
status `ASSIGNED`/`OTW`). Sisi Kilang (`POST /api/kilang/pickup/status`, buat penugasan) dan sisi
Driver (app lapangan terpisah, `PATCH`/`GET /api/driver/pickup/status`) di luar cakupan app mobile
Klien/Agen ini.

## Belum ada di API-DOC (sengaja tidak dibuat / memakai placeholder)

Rute jemput (assignment Kilang→Driver di luar app ini), upgrade Klien→Agen, mode tamu
(`/api/user/agen` butuh token), OTP, harga jelantah, ambang stok, riwayat stok, koreksi stok,
jarak/lokasi pengguna. Nilai harga & ambang sementara ada di `AppConfig.kt`.

## Catatan struktur proyek

Kalau paket ini diambil dari arsip lama: folder `shared/` sempat berisi salinan ganda seluruh
proyek (root ter-nested di dalam `shared/shared/`). Sudah dirapikan di sini — `shared/`
sekarang cuma berisi `build.gradle.kts` modul KMP + `src/`, seperti struktur normal.

## Font

Sora, Manrope, IBM Plex Mono (SIL OFL 1.1) — lisensi di `licenses/fonts/`.
