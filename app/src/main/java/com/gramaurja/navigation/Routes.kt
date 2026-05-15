package com.gramaurja.navigation

sealed class Route(val value: String) {
    data object Splash : Route("splash")
    data object Language : Route("language")
    data object Login : Route("login")
    data object Home : Route("home")
    data object PumpTimer : Route("pump_timer")
    data object Alerts : Route("alerts")
    data object Reports : Route("reports")
    data object Profile : Route("profile")
    data object Analytics : Route("analytics")
}
