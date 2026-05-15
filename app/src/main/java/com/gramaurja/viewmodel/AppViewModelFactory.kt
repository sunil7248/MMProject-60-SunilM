package com.gramaurja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gramaurja.GramaUrjaApplication

class AppViewModelFactory(
    private val application: GramaUrjaApplication
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repositories = application.repositories
        val preferences = application.preferencesRepository
        return when {
            modelClass.isAssignableFrom(AppStateViewModel::class.java) ->
                AppStateViewModel(repositories, preferences) as T
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) ->
                OnboardingViewModel(repositories, preferences) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(repositories) as T
            modelClass.isAssignableFrom(PumpTimerViewModel::class.java) ->
                PumpTimerViewModel(repositories) as T
            modelClass.isAssignableFrom(AlertsViewModel::class.java) ->
                AlertsViewModel(repositories) as T
            modelClass.isAssignableFrom(ReportsViewModel::class.java) ->
                ReportsViewModel(repositories) as T
            modelClass.isAssignableFrom(AnalyticsViewModel::class.java) ->
                AnalyticsViewModel(repositories) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
