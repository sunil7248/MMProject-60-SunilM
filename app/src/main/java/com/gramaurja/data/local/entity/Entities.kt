package com.gramaurja.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val phone: String,
    val village: String,
    val zone: String,
    val password: String
)

@Entity(tableName = "power_status")
data class PowerStatusEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val status: String,
    val timestamp: Long,
    val zone: String
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val issueType: String,
    val description: String,
    val imageUri: String = "",
    val timestamp: Long
)

@Entity(tableName = "pump_history")
data class PumpHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cropType: String,
    val duration: String,
    val timestamp: Long
)

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    val timestamp: Long
)

@Entity(tableName = "zone_selections")
data class ZoneSelectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val district: String,
    val village: String,
    val zone: String,
    val selectedAt: Long,
    val isActive: Boolean = true
)
