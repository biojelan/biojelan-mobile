package id.biojelan.app.ui

import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.data.repository.TxStatus
import id.biojelan.app.ui.components.ChipKind

/** Sudut pandang pengguna, memengaruhi label status & nama pihak lawan. */
enum class Viewer { Klien, Agen }

fun TxStatus.chipKind(): ChipKind = when (this) {
    TxStatus.Pending -> ChipKind.Pending
    TxStatus.Accepted -> ChipKind.Done
    TxStatus.Rejected -> ChipKind.Cancelled
    TxStatus.CancelRequested -> ChipKind.Pending
    TxStatus.Cancelled -> ChipKind.Cancelled
    TxStatus.Unknown -> ChipKind.Neutral
}

fun TxStatus.label(viewer: Viewer): String = when (this) {
    TxStatus.Pending -> if (viewer == Viewer.Klien) "Menunggu Anda" else "Menunggu Klien"
    TxStatus.Accepted -> if (viewer == Viewer.Klien) "Selesai" else "Diterima"
    TxStatus.Rejected -> "Ditolak"
    TxStatus.CancelRequested -> if (viewer == Viewer.Klien) "Agen minta batal" else "Menunggu Klien"
    TxStatus.Cancelled -> "Dibatalkan"
    TxStatus.Unknown -> "—"
}

/** Nama pihak lawan: Agen bagi Klien, Klien bagi Agen. Respons tiap endpoint memakai key berbeda. */
fun TransactionDto.counterpartName(viewer: Viewer, agenNameLookup: (String) -> String? = { null }): String =
    when (viewer) {
        Viewer.Klien -> agenName.ifBlank { agenNameLookup(agenId).orEmpty() }.ifBlank { "Agen" }
        Viewer.Agen -> clientName.ifBlank { "Klien" }
    }
