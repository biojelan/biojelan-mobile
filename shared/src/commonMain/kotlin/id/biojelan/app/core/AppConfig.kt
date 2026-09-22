package id.biojelan.app.core

/**
 * Konfigurasi aplikasi. Semua angka bisnis yang BELUM punya endpoint di API-DOC
 * dikumpulkan di sini supaya gampang diganti begitu backend menyediakannya.
 */
object AppConfig {
    /** Base URL dari API-DOC. Masih http:// — ganti ke https:// begitu backend siap. */
    const val BASE_URL = "http://biojelan.id"

    const val CONNECT_TIMEOUT_MS = 10_000L
    const val REQUEST_TIMEOUT_MS = 20_000L

    /**
     * TODO(backend): belum ada endpoint harga. Prototype menampilkan "harga aktif dari Kilang".
     * Sampai ada, dipakai nilai default ini (lihat [id.biojelan.app.data.repository.PriceProvider]).
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
