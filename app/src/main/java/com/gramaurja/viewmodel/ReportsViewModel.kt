package com.gramaurja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja.data.local.entity.ReportEntity
import com.gramaurja.data.repository.AppRepositories
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReportsUiState(
    val issueType: String = "Transformer fault",
    val description: String = "",
    val imageUri: String = "",
    val reports: List<ReportEntity> = emptyList()
)

class ReportsViewModel(
    private val repositories: AppRepositories
) : ViewModel() {
    val events = MutableSharedFlow<String>()
    private val formState = MutableStateFlow(ReportsUiState())

    val uiState: StateFlow<ReportsUiState> = combine(
        formState,
        repositories.reportRepository.observeReports()
    ) { form, reports ->
        form.copy(reports = reports)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ReportsUiState())

    fun setIssueType(value: String) = formState.update { it.copy(issueType = value) }
    fun setDescription(value: String) = formState.update { it.copy(description = value) }
    fun setImageUri(value: String) = formState.update { it.copy(imageUri = value) }

    fun submitReport() {
        viewModelScope.launch {
            val state = formState.value
            if (state.description.isBlank()) {
                events.emit("Please add issue details before submitting.")
                return@launch
            }

            repositories.reportRepository.submitReport(
                issueType = state.issueType,
                description = state.description,
                imageUri = state.imageUri,
                timestamp = System.currentTimeMillis()
            )
            repositories.alertRepository.pushMessage(
                message = "${state.issueType} report saved locally for review",
                timestamp = System.currentTimeMillis()
            )
            formState.value = ReportsUiState()
            events.emit("Issue report stored offline.")
        }
    }
}
