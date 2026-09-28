package id.biojelan.app.data.repository

import id.biojelan.app.core.AppConfig

/**
 * Sumber harga jelantah per liter.
 *
 * TODO(backend): API-DOC belum punya endpoint harga, padahal `POST /api/agen-transaction` menerima
 * `price` & `total_price` dari klien. Idealnya server yang menghitung harga (bukan app), lalu app
 * cukup menampilkan. Sampai endpoint ada, pakai nilai default dari [AppConfig].
 */
interface PriceProvider {
    suspend fun pricePerLiter(): Long
}

class ConfigPriceProvider : PriceProvider {
    override suspend fun pricePerLiter(): Long = AppConfig.DEFAULT_PRICE_PER_LITER
}
