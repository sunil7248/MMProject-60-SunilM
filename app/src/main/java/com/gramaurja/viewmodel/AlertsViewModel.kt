package com.gramaurja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja.data.local.entity.AlertEntity
import com.gramaurja.data.local.entity.PowerStatusEntity
import com.gramaurja.data.repository.AppRepositories
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class AlertsUiState(
    val alerts: List<AlertEntity> = emptyList(),
    val powerUpdates: List<PowerStatusEntity> = emptyList()
)

class AlertsViewModel(
    repositories: AppRepositories
) : ViewModel() {
    val uiState: StateFlow<AlertsUiState> = combine(
        repositories.alertRepository.observeAlerts(),
        repositories.alertRepository.observePowerUpdates()
    ) { alerts, updates ->
        AlertsUiState(alerts = alerts, powerUpdates = updates)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AlertsUiState())
}
