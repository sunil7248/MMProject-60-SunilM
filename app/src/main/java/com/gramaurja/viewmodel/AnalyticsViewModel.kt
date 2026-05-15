package com.gramaurja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja.data.local.entity.PowerStatusEntity
import com.gramaurja.data.repository.AppRepositories
import com.gramaurja.utils.formatTimestamp
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

data class AnalyticsUiState(
    val totalUpdates: Int = 0,
    val outageCountToday: Int = 0,
    val currentStatus: String = "OFF",
    val weeklyOutageTrend: List<Pair<String, Int>> = emptyList(),
    val timeline: List<String> = emptyList(),
    val allStatuses: List<PowerStatusEntity> = emptyList()
)

class AnalyticsViewModel(
    repositories: AppRepositories
) : ViewModel() {
    val uiState: StateFlow<AnalyticsUiState> = combine(
        repositories.powerRepository.observeAllStatuses(),
        repositories.powerRepository.observeLatestStatus()
    ) { allStatuses, latest ->
        val today = LocalDate.now()
        val week = (0..6).map { today.minusDays((6 - it).toLong()) }
        val grouped = week.map { date ->
            val count = allStatuses.count {
                it.status == "OFF" &&
                    Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDate() == date
            }
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH) to count
        }

        AnalyticsUiState(
            totalUpdates = allStatuses.size,
            outageCountToday = grouped.lastOrNull()?.second ?: 0,
            currentStatus = latest?.status ?: "OFF",
            weeklyOutageTrend = grouped,
            timeline = allStatuses.take(12).map { "Power ${it.status} at ${formatTimestamp(it.timestamp)}" },
            allStatuses = allStatuses
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AnalyticsUiState())
}
