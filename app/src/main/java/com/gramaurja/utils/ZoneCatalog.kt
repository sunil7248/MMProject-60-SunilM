package com.gramaurja.utils

data class ZoneOption(
    val district: String,
    val village: String,
    val zone: String
)

val zoneCatalog = listOf(
    ZoneOption("Bengaluru Urban", "Anekal", "Anekal Feeder 1"),
    ZoneOption("Bengaluru Rural", "Devanahalli", "Devanahalli Feeder 2"),
    ZoneOption("Mandya", "Maddur", "Maddur Feeder North"),
    ZoneOption("Mandya", "Malavalli", "Malavalli Pump Line"),
    ZoneOption("Mysuru", "Hunsur", "Hunsur Feeder East"),
    ZoneOption("Mysuru", "Nanjangud", "Nanjangud Rural Line"),
    ZoneOption("Tumakuru", "Tiptur", "Tiptur Feeder South"),
    ZoneOption("Tumakuru", "Gubbi", "Gubbi Transformer Line"),
    ZoneOption("Hassan", "Arsikere", "Arsikere Feeder 3"),
    ZoneOption("Hassan", "Belur", "Belur Rural Supply"),
    ZoneOption("Belagavi", "Gokak", "Gokak Agriculture Line"),
    ZoneOption("Belagavi", "Athani", "Athani Feeder West"),
    ZoneOption("Dharwad", "Hubballi", "Hubballi Village Grid"),
    ZoneOption("Shivamogga", "Sagara", "Sagara Pump Circuit"),
    ZoneOption("Ballari", "Hospet", "Hospet Feeder 4"),
    ZoneOption("Chikkamagaluru", "Kadur", "Kadur Rural East"),
    ZoneOption("Kolar", "Mulbagal", "Mulbagal Feeder South"),
    ZoneOption("Vijayapura", "Indi", "Indi Agriculture Supply")
)

fun findZoneOption(zone: String): ZoneOption? = zoneCatalog.firstOrNull { it.zone == zone }
