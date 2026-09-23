package id.biojelan.app.data.repository

import id.biojelan.app.data.remote.ApiClient
import id.biojelan.app.data.remote.ApiResult
import id.biojelan.app.data.remote.PickupStatusDto
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

enum class PickupStatus {
    Assigned, OnTheWay, Completed, Cancelled, Unknown;

    companion object {
        fun from(raw: String): PickupStatus = when (raw.trim().uppercase()) {
            "ASSIGNED" -> Assigned
            "OTW" -> OnTheWay
            "COMPLETED" -> Completed
            "CANCELLED", "CANCELED" -> Cancelled
            else -> Unknown
        }
    }
}

val PickupStatusDto.pickupStatus: PickupStatus get() = PickupStatus.from(status)

/**
 * Endpoint di pickup.md. Hanya sisi Agen yang relevan untuk app mobile ini — Kilang membuat
 * penugasan dari dashboard web, Driver punya app lapangan terpisah (lihat README).
 */
class PickupRepository(
    private val api: ApiClient,
    private val json: Json,
) {
    /**
     * GET /api/agen/pickup/status — status penjemputan Driver terbaru untuk Agen yang sedang
     * login. Dokumen tidak menjelaskan respons saat belum ada penugasan, jadi data kosong/bukan
     * objek -> null (ditampilkan sebagai "belum ada penjemputan dijadwalkan").
     */
    suspend fun agenStatus(): ApiResult<PickupStatusDto?> =
        api.call(HttpMethod.Get, "/api/agen/pickup/status") { data -> decodeOptional(data) }

    private fun decodeOptional(data: JsonElement?): PickupStatusDto? =
        if (data is JsonObject && data.isNotEmpty()) json.decodeFromJsonElement(PickupStatusDto.serializer(), data)
        else null
}
