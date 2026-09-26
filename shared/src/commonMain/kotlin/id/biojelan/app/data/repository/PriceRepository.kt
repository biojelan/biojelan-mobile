package id.biojelan.app.data.repository

import id.biojelan.app.data.remote.ApiClient
import id.biojelan.app.data.remote.ApiResult
import id.biojelan.app.data.remote.PriceDto
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * Endpoint di price.md. Sisi Kilang (`POST /api/price`, update harga) di luar cakupan app
 * mobile ini — perubahan harga dilakukan dari dashboard web Kilang.
 */
class PriceRepository(
    private val api: ApiClient,
    private val json: Json,
) {
    /**
     * GET /api/price — harga jelantah per liter yang sedang berlaku. Dokumentasi tidak
     * mencantumkan `Authorization` untuk endpoint ini (beda dengan POST-nya), jadi dipanggil
     * tanpa token supaya tetap bisa diakses dari layar tamu/guest.
     */
    suspend fun current(): ApiResult<PriceDto?> =
        api.call(HttpMethod.Get, "/api/price", authenticated = false) { data -> decodeOptional(data) }

    private fun decodeOptional(data: JsonElement?): PriceDto? =
        if (data is JsonObject && data.isNotEmpty()) json.decodeFromJsonElement(PriceDto.serializer(), data)
        else null
}
