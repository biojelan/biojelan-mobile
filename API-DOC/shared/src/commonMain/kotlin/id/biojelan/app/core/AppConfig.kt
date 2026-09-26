package id.biojelan.app.core

/**
 * Konfigurasi aplikasi. Semua angka bisnis yang BELUM punya endpoint di API-DOC
 * dikumpulkan di sini supaya gampang diganti begitu backend menyediakannya.
 */
object AppConfig {
    /** Base URL dari API-DOC. Masih http:// — ganti ke https:// begitu backend siap. */
    const val BASE_URL = "https://biojelan.callmeoda.web.id"

    const val CONNECT_TIMEOUT_MS = 10_000L
    const val REQUEST_TIMEOUT_MS = 20_000L

    /**
     * Aktifkan mode fallback: API call yang gagal (network/server error)
     * akan otomatis dijawab dengan mock data, supaya app tetap bisa
     * dipakai meski backend belum di-deploy.
     *
     * Set ke `false` begitu backend sudah live dan stabil.
     */
    const val ENABLE_FALLBACK = true

    /** Mode tema default: "system" (ikut OS), "light", atau "dark". */
    const val DEFAULT_THEME = "system"

    /** Kode bahasa default: "id" (Indonesia) atau "en" (English). */
    const val DEFAULT_LANGUAGE = "id"

    /**
     * Nilai cadangan kalau `GET /api/price` gagal dipanggil (offline/error) atau server belum
     * punya data harga aktif. Lihat [id.biojelan.app.data.repository.ApiPriceProvider].
     */
    const val DEFAULT_PRICE_PER_LITER = 6_500L

    /**
     * TODO(backend): ambang stok Agen dikonfigurasi Superadmin di Kilang (default 500 L di prototype).
     * Belum ada endpoint konfigurasi, jadi sementara konstanta.
     */
    const val STOCK_THRESHOLD_LITER = 500.0

    /** Kode hari yang dipakai API untuk `open_day` / `open_days`, urut Senin–Minggu. */
    val WEEK_DAYS = listOf("senin", "selasa", "rabu", "kamis", "jumat", "sabtu", "minggu")
}
