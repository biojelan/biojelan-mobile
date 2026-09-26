package id.biojelan.app.data.repository

import id.biojelan.app.core.AppConfig
import id.biojelan.app.data.remote.ApiResult

/**
 * Sumber harga jelantah per liter.
 */
interface PriceProvider {
    suspend fun pricePerLiter(): Long
}

/**
 * GET /api/price (price.md). Kalau panggilan gagal, jaringan bermasalah, atau server balikin
 * `price_per_liter` <= 0 (data belum di-set Kilang), jatuh ke [AppConfig.DEFAULT_PRICE_PER_LITER]
 * supaya layar harga tidak pernah menampilkan Rp 0 atau macet nunggu.
 */
class ApiPriceProvider(private val repository: PriceRepository) : PriceProvider {
    override suspend fun pricePerLiter(): Long {
        val result = repository.current()
        val price = if (result is ApiResult.Success) result.data?.pricePerLiter else null
        return if (price != null && price > 0) price else AppConfig.DEFAULT_PRICE_PER_LITER
    }
}

/** Dipakai saat endpoint harga sengaja tidak dipanggil (mis. mode demo) — lihat [AppConfig]. */
class ConfigPriceProvider : PriceProvider {
    override suspend fun pricePerLiter(): Long = AppConfig.DEFAULT_PRICE_PER_LITER
}
