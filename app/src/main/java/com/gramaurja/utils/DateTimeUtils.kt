package com.gramaurja.utils

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatTimestamp(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM, hh:mm a")
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun timeAgo(timestamp: Long): String {
    val duration = Duration.between(Instant.ofEpochMilli(timestamp), Instant.now())
    val minutes = duration.toMinutes()
    return when {
        minutes < 1 -> "Updated just now"
        minutes < 60 -> "Updated $minutes mins ago"
        minutes < 1440 -> "Updated ${duration.toHours()} hrs ago"
        else -> "Updated ${duration.toDays()} days ago"
    }
}
