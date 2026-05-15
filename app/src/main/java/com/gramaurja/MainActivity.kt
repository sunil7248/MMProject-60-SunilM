package com.gramaurja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gramaurja.navigation.GramaUrjaNavGraph
import com.gramaurja.ui.theme.GramaUrjaTheme
import com.gramaurja.utils.AppLanguageProvider
import com.gramaurja.viewmodel.AppStateViewModel
import com.gramaurja.viewmodel.AppViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val application = LocalContext.current.applicationContext as GramaUrjaApplication
            val appStateViewModel: AppStateViewModel = viewModel(
                factory = AppViewModelFactory(application)
            )
            val shellState = appStateViewModel.uiState.collectAsState().value

            GramaUrjaTheme(darkTheme = false) {
                AppLanguageProvider(languageCode = shellState.languageCode) {
                    GramaUrjaNavGraph(
                        application = application,
                        appStateViewModel = appStateViewModel
                    )
                }
            }
        }
    }
}
