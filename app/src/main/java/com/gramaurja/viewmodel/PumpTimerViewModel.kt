package com.gramaurja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja.data.local.entity.PumpHistoryEntity
import com.gramaurja.data.repository.AppRepositories
import com.gramaurja.utils.calculatePumpRuntimeMinutes
import com.gramaurja.utils.formatMinutes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PumpTimerUiState(
    val cropType: String = "Paddy",
    val motorHp: String = "",
    val waterRequirement: String = "",
    val result: String = "Recommended runtime will appear here.",
    val progress: Float = 0f,
    val history: List<PumpHistoryEntity> = emptyList()
)

class PumpTimerViewModel(
    repositories: AppRepositories
) : ViewModel() {
    private val history = repositories.pumpRepository.observeHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val form = MutableStateFlow(PumpTimerUiState())
    val uiState: StateFlow<PumpTimerUiState> = form.asStateFlow()

    init {
        viewModelScope.launch {
            history.collect { entries ->
                form.update { it.copy(history = entries) }
            }
        }
    }

    private val pumpRepository = repositories.pumpRepository

    fun setCrop(value: String) = form.update { it.copy(cropType = value) }
    fun setMotorHp(value: String) = form.update { it.copy(motorHp = value) }
    fun setWaterRequirement(value: String) = form.update { it.copy(waterRequirement = value) }

    fun calculate() {
        viewModelScope.launch {
            val state = form.value
            val hp = state.motorHp.toDoubleOrNull() ?: 1.0
            val water = state.waterRequirement.toDoubleOrNull() ?: 1.0
            val durationMinutes = calculatePumpRuntimeMinutes(state.cropType, hp, water)
            val formatted = formatMinutes(durationMinutes)

            form.update {
                it.copy(
                    result = "Recommended runtime: $formatted",
                    progress = (durationMinutes / 180f).coerceIn(0f, 1f)
                )
            }

            pumpRepository.saveCalculation(
                cropType = state.cropType,
                duration = formatted,
                timestamp = System.currentTimeMillis()
            )
        }
    }
}
