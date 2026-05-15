package com.gramaurja.utils

import kotlin.math.max
import kotlin.math.roundToInt

fun calculatePumpRuntimeMinutes(cropType: String, motorHp: Double, waterRequirement: Double): Int {
    val cropFactor = when (cropType) {
        "Paddy" -> 1.35
        "Sugarcane" -> 1.25
        "Vegetables" -> 0.95
        "Millets" -> 0.75
        else -> 1.0
    }
    return ((waterRequirement * cropFactor) / max(motorHp, 0.5) * 12).roundToInt().coerceAtLeast(10)
}

fun formatMinutes(minutes: Int): String {
    val hours = minutes / 60
    val mins = minutes % 60
    return buildString {
        if (hours > 0) append("$hours hr ")
        append("$mins mins")
    }.trim()
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 8 &&
        password.any(Char::isUpperCase) &&
        password.any(Char::isLowerCase) &&
        password.any(Char::isDigit)
}

fun isValidPhone(phone: String): Boolean {
    return phone.length == 10 && phone.all(Char::isDigit) && phone.firstOrNull() in '6'..'9'
}
