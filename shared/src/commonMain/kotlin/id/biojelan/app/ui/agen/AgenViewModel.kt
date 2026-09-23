package id.biojelan.app.ui.agen

import androidx.lifecycle.viewModelScope
import id.biojelan.app.core.AppConfig
import id.biojelan.app.core.isToday
import id.biojelan.app.core.parseIsoMillis
import id.biojelan.app.data.remote.ApiResult
import id.biojelan.app.data.remote.PickupStatusDto
import id.biojelan.app.data.remote.TransactionDto
import id.biojelan.app.data.remote.UserDto
import id.biojelan.app.data.repository.PickupRepository
import id.biojelan.app.data.repository.PriceProvider
import id.biojelan.app.data.repository.SessionManager
import id.biojelan.app.data.repository.SessionState
import id.biojelan.app.data.repository.TransactionRepository
import id.biojelan.app.data.repository.TxStatus
import id.biojelan.app.data.repository.UserRepository
import id.biojelan.app.data.repository.toUpdateRequest
import id.biojelan.app.data.repository.txStatus
import id.biojelan.app.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AgenUiState(
    val transactions: List<TransactionDto> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null,
    val price: Long = AppConfig.DEFAULT_PRICE_PER_LITER,
    val creating: Boolean = false,
    val togglingOpen: Boolean = false,
    val pickup: PickupStatusDto? = null,
    val pickupLoading: Boolean = true,
) {
    private val todays get() = transactions.filter { isToday(it.createdAt) && it.txStatus != TxStatus.Cancelled }
    val todayCount: Int get() = todays.size
    val todayLiters: Double get() = todays.sumOf { it.volumeLiter }
    val todayValue: Long get() = todays.sumOf { it.totalPrice }
}

class AgenViewModel(
    private val users: UserRepository,
    private val transactions: TransactionRepository,
    private val session: SessionManager,
    private val priceProvider: PriceProvider,
    private val pickups: PickupRepository,
) : BaseViewModel() {
    private val _state = MutableStateFlow(AgenUiState())
    val state: StateFlow<AgenUiState> = _state.asStateFlow()

    /** Profil Agen terbaru (stok, status buka/tutup, dst.) dari sesi. */
    val user: StateFlow<UserDto?> = session.state
        .map { (it as? SessionState.LoggedIn)?.user }
        .stateIn(viewModelScope, SharingStarted.Eagerly, session.currentUser)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            // Stok (`stock_liter`) ikut di GET /api/user, jadi ambil ulang profil bersamaan dengan transaksi.
            launch { users.refreshUser() }
            launch {
                val price = priceProvider.pricePerLiter()
                _state.update { it.copy(price = price) }
            }
            launch { refreshPickup() }
            val agenId = session.currentUser?.agen?.agenId
            if (agenId.isNullOrBlank()) {
                _state.update { it.copy(loading = false, error = "Data Agen tidak ditemukan pada akun ini.") }
                return@launch
            }
            when (val result = transactions.agenTransactions(agenId)) {
                is ApiResult.Success -> {
                    val sorted = result.data.sortedByDescending { tx -> parseIsoMillis(tx.createdAt) ?: 0L }
                    _state.update { it.copy(transactions = sorted, loading = false) }
                }
                is ApiResult.Failure -> _state.update { it.copy(loading = false, error = result.message) }
            }
        }
    }

    /** POST /api/agen-transaction. Klien lalu menerima/membatalkan lewat app-nya. */
    fun createTransaction(klienId: String, klienName: String, volumeLiter: Double, onSuccess: () -> Unit) {
        val agenId = session.currentUser?.agen?.agenId ?: return
        if (_state.value.creating) return
        viewModelScope.launch {
            _state.update { it.copy(creating = true) }
            val price = _state.value.price
            when (val result = transactions.createAsAgen(agenId, klienId, klienName, volumeLiter, price)) {
                is ApiResult.Success -> {
                    toast("Transaksi dikirim ke ${klienName.trim()} — menunggu konfirmasi Klien")
                    onSuccess()
                    refresh()
                }
                is ApiResult.Failure -> toast(result.message)
            }
            _state.update { it.copy(creating = false) }
        }
    }

    /** Buka/tutup toko lewat PATCH /api/user (agen.is_open). */
    fun toggleOpen() {
        val current = session.currentUser ?: return
        val agen = current.agen ?: return
        if (_state.value.togglingOpen) return
        viewModelScope.launch {
            _state.update { it.copy(togglingOpen = true) }
            val nextOpen = !agen.isOpen
            when (val result = users.updateUser(current.toUpdateRequest(isOpen = nextOpen))) {
                is ApiResult.Success -> toast(if (nextOpen) "Toko dibuka — Klien bisa melihat Anda." else "Toko ditutup sementara.")
                is ApiResult.Failure -> toast(result.message)
            }
            _state.update { it.copy(togglingOpen = false) }
        }
    }

    /** Ikon jam di topbar tab Stok (`#screen-agen-stok`) — di prototype cuma munculin info, bukan aksi nyata. */
    fun infoStockHistory() = toast("Riwayat lengkap pergerakan stok")

    /** Link "Ada selisih catatan stok?" di bawah tab Stok — koreksi stok belum ada endpoint-nya. */
    fun infoStockCorrection() = toast("Fitur koreksi stok belum tersedia — hubungi Kilang untuk perbaikan catatan.")

    /**
     * GET /api/agen/pickup/status. Dipanggil bersamaan dengan [refresh]; kegagalan di sini tidak
     * memblokir tampilan transaksi/profil — status penjemputan cuma disembunyikan (null).
     */
    private suspend fun refreshPickup() {
        _state.update { it.copy(pickupLoading = true) }
        when (val result = pickups.agenStatus()) {
            is ApiResult.Success -> _state.update { it.copy(pickup = result.data, pickupLoading = false) }
            is ApiResult.Failure -> _state.update { it.copy(pickup = null, pickupLoading = false) }
        }
    }
}
