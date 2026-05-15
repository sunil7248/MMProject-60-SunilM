package com.gramaurja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja.data.repository.AppRepositories
import com.gramaurja.data.repository.UserPreferencesRepository
import com.gramaurja.utils.findZoneOption
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AppShellUiState(
    val name: String = "",
    val phone: String = "",
    val village: String = "",
    val currentZoneLabel: String = "Select transformer zone",
    val currentDistrict: String = "",
    val languageCode: String = "",
    val activeTransformerZones: List<String> = emptyList()
)

class AppStateViewModel(
    private val repositories: AppRepositories,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<AppShellUiState> = combine(
        repositories.userRepository.observeUser(),
        repositories.zoneRepository.observeCurrentZone(),
        preferencesRepository.selectedLanguage,
        repositories.powerRepository.observeActiveZones()
    ) { user, zone, language, activeZones ->
        AppShellUiState(
            name = user?.name.orEmpty(),
            phone = user?.phone.orEmpty(),
            village = user?.village.orEmpty(),
            currentZoneLabel = zone?.zone ?: user?.zone ?: "Select transformer zone",
            currentDistrict = zone?.district.orEmpty(),
            languageCode = language,
            activeTransformerZones = activeZones
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppShellUiState())

    fun setLanguage(code: String) {
        viewModelScope.launch {
            preferencesRepository.saveLanguage(code)
        }
    }

    fun changeZone(zone: String) {
        viewModelScope.launch {
            val selected = findZoneOption(zone) ?: return@launch
            repositories.zoneRepository.selectZone(
                district = selected.district,
                village = selected.village,
                zone = selected.zone,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesRepository.setSignedIn(false)
        }
    }
}
