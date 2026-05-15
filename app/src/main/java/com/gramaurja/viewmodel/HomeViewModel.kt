package com.gramaurja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja.data.local.entity.PowerStatusEntity
import com.gramaurja.data.repository.AppRepositories
import com.gramaurja.utils.formatTimestamp
import com.gramaurja.utils.timeAgo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class HomeUiState(
    val currentStatus: String = "OFF",
    val lastUpdated: String = "No updates yet",
    val freshness: String = "Waiting for first community update",
    val statusHistory: List<PowerStatusEntity> = emptyList(),
    val totalOutagesToday: Int = 0,
    val currentZone: String = "No zone selected",
    val energyTip: String = "Run motors during stable feeder windows to reduce restart losses.",
    val weatherMessage: String = "Weather integration will appear here in a future update."
)

class HomeViewModel(
    private val repositories: AppRepositories
) : ViewModel() {
    val events = MutableSharedFlow<String>()

    private val tips = listOf(
        "Run irrigation in short cycles to avoid dry patching in high-heat fields.",
        "Switch off idle starters to protect motors during voltage dips.",
        "Use community status updates before starting long pump sessions.",
        "Check cable joints regularly before monsoon demand peaks."
    )

    val uiState: StateFlow<HomeUiState> = combine(
        repositories.powerRepository.observeLatestStatus(),
        repositories.powerRepository.observeRecentStatuses(),
        repositories.zoneRepository.observeCurrentZone()
    ) { latest, history, zone ->
        val today = LocalDate.now()
        val outages = history.count {
            it.status == "OFF" && Instant.ofEpochMilli(it.timestamp)
                .atZone(ZoneId.systemDefault()).toLocalDate() == today
        }
        HomeUiState(
            currentStatus = latest?.status ?: "OFF",
            lastUpdated = latest?.timestamp?.let(::formatTimestamp) ?: "No updates yet",
            freshness = latest?.timestamp?.let(::timeAgo) ?: "Waiting for first community update",
            statusHistory = history,
            totalOutagesToday = outages,
            currentZone = zone?.zone ?: "No zone selected",
            energyTip = tips[(history.size % tips.size)],
            weatherMessage = "Stay ready: weather alerts can later be mapped to ${zone?.district ?: "your"} feeder."
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    fun setPowerStatus(status: String) {
        viewModelScope.launch {
            val zone = repositories.zoneRepository.observeCurrentZone().first()?.zone ?: "Community Zone"
            repositories.powerRepository.updatePower(
                status = status,
                zone = zone,
                timestamp = System.currentTimeMillis()
            )
            events.emit("Power marked $status for $zone")
        }
    }
}
