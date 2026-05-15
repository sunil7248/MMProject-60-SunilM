package com.gramaurja.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gramaurja.GramaUrjaApplication
import com.gramaurja.ui.components.BrandedSnackbar
import com.gramaurja.ui.components.GramaUrjaBottomBar
import com.gramaurja.ui.components.GramaUrjaTopBar
import com.gramaurja.ui.screens.alerts.AlertsScreen
import com.gramaurja.ui.screens.analytics.AnalyticsScreen
import com.gramaurja.ui.screens.auth.UserDetailsScreen
import com.gramaurja.ui.screens.home.HomeScreen
import com.gramaurja.ui.screens.language.LanguageSelectionScreen
import com.gramaurja.ui.screens.profile.ProfileScreen
import com.gramaurja.ui.screens.pump.PumpTimerScreen
import com.gramaurja.ui.screens.reports.ReportIssueScreen
import com.gramaurja.ui.screens.splash.SplashScreen
import com.gramaurja.viewmodel.AlertsViewModel
import com.gramaurja.viewmodel.AnalyticsViewModel
import com.gramaurja.viewmodel.AppStateViewModel
import com.gramaurja.viewmodel.AppViewModelFactory
import com.gramaurja.viewmodel.HomeViewModel
import com.gramaurja.viewmodel.OnboardingEvent
import com.gramaurja.viewmodel.OnboardingViewModel
import com.gramaurja.viewmodel.PumpTimerViewModel
import com.gramaurja.viewmodel.ReportsViewModel

@Composable
fun GramaUrjaNavGraph(
    application: GramaUrjaApplication,
    appStateViewModel: AppStateViewModel
) {
    val factory = remember(application) { AppViewModelFactory(application) }
    val onboardingViewModel: OnboardingViewModel = viewModel(factory = factory)
    val homeViewModel: HomeViewModel = viewModel(factory = factory)
    val pumpTimerViewModel: PumpTimerViewModel = viewModel(factory = factory)
    val alertsViewModel: AlertsViewModel = viewModel(factory = factory)
    val reportsViewModel: ReportsViewModel = viewModel(factory = factory)
    val analyticsViewModel: AnalyticsViewModel = viewModel(factory = factory)

    val navController = rememberNavController()
    val snackbars = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val route = currentDestination?.route.orEmpty()

    val chromeRoutes = setOf(
        Route.Home.value,
        Route.PumpTimer.value,
        Route.Reports.value,
        Route.Profile.value,
        Route.Analytics.value,
        Route.Alerts.value
    )
    val bottomRoutes = setOf(
        Route.Home.value,
        Route.PumpTimer.value,
        Route.Reports.value,
        Route.Analytics.value,
        Route.Alerts.value
    )

    val shellState by appStateViewModel.uiState.collectAsStateWithLifecycle()
    val showChrome = currentDestination?.hierarchy?.any { it.route in chromeRoutes } == true
    val showBottomBar = currentDestination?.hierarchy?.any { it.route in bottomRoutes } == true

    LaunchedEffect(homeViewModel.events) {
        homeViewModel.events.collect { snackbars.showSnackbar(it) }
    }

    LaunchedEffect(reportsViewModel.events) {
        reportsViewModel.events.collect { snackbars.showSnackbar(it) }
    }

    LaunchedEffect(onboardingViewModel.events) {
        onboardingViewModel.events.collect { event ->
            when (event) {
                is OnboardingEvent.Message -> snackbars.showSnackbar(event.text)
                is OnboardingEvent.Navigate -> {
                    navController.navigate(event.route) {
                        popUpTo(Route.Login.value) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (showChrome) {
                GramaUrjaTopBar(
                    currentZone = shellState.currentZoneLabel,
                    onAlertsClick = { navController.navigate(Route.Alerts.value) },
                    onProfileClick = { navController.navigate(Route.Profile.value) }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                GramaUrjaBottomBar(
                    currentRoute = route,
                    onNavigate = { target ->
                        navController.navigate(target) {
                            launchSingleTop = true
                            restoreState = true
                            if (target == Route.Home.value) {
                                popUpTo(Route.Home.value) {
                                    inclusive = false
                                    saveState = false
                                }
                            } else {
                                popUpTo(Route.Home.value) { saveState = true }
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbars) { data ->
                BrandedSnackbar(snackbarData = data)
            }
        },
        floatingActionButton = {}
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash.value,
            modifier = Modifier.padding(padding)
        ) {
            composable(Route.Splash.value) {
                SplashScreen(
                    nextRoute = onboardingViewModel.startDestination.collectAsStateWithLifecycle().value,
                    onFinished = { target ->
                        navController.navigate(target) {
                            popUpTo(Route.Splash.value) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.Language.value) {
                LanguageSelectionScreen(
                    selectedLanguage = shellState.languageCode,
                    onSelectLanguage = {
                        onboardingViewModel.saveLanguage(it)
                        navController.navigate(Route.Login.value) {
                            popUpTo(Route.Language.value) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.Login.value) {
                UserDetailsScreen(
                    uiState = onboardingViewModel.uiState.collectAsStateWithLifecycle().value,
                    onModeChange = onboardingViewModel::setAuthMode,
                    onNameChange = onboardingViewModel::updateName,
                    onPhoneChange = onboardingViewModel::updatePhone,
                    onZoneChange = onboardingViewModel::updateZone,
                    onPasswordChange = onboardingViewModel::updatePassword,
                    onConfirmPasswordChange = onboardingViewModel::updateConfirmPassword,
                    onForgotPasswordClick = onboardingViewModel::startPasswordReset,
                    onSubmit = onboardingViewModel::submitAuth
                )
            }
            composable(Route.Home.value) {
                HomeScreen(
                    uiState = homeViewModel.uiState.collectAsStateWithLifecycle().value,
                    onSetPower = homeViewModel::setPowerStatus
                )
            }
            composable(Route.PumpTimer.value) {
                PumpTimerScreen(
                    uiState = pumpTimerViewModel.uiState.collectAsStateWithLifecycle().value,
                    onCropChange = pumpTimerViewModel::setCrop,
                    onMotorHpChange = pumpTimerViewModel::setMotorHp,
                    onWaterRequirementChange = pumpTimerViewModel::setWaterRequirement,
                    onCalculate = pumpTimerViewModel::calculate
                )
            }
            composable(Route.Alerts.value) {
                AlertsScreen(uiState = alertsViewModel.uiState.collectAsStateWithLifecycle().value)
            }
            composable(Route.Reports.value) {
                ReportIssueScreen(
                    uiState = reportsViewModel.uiState.collectAsStateWithLifecycle().value,
                    onIssueTypeChange = reportsViewModel::setIssueType,
                    onDescriptionChange = reportsViewModel::setDescription,
                    onImageSelected = reportsViewModel::setImageUri,
                    onSubmit = reportsViewModel::submitReport
                )
            }
            composable(Route.Profile.value) {
                ProfileScreen(
                    uiState = shellState,
                    onLanguageChange = appStateViewModel::setLanguage,
                    onChangeZone = appStateViewModel::changeZone,
                    onLogout = {
                        appStateViewModel.logout()
                        navController.navigate(Route.Login.value) {
                            popUpTo(Route.Home.value) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.Analytics.value) {
                AnalyticsScreen(uiState = analyticsViewModel.uiState.collectAsStateWithLifecycle().value)
            }
        }
    }
}
