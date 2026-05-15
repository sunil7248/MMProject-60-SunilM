package com.gramaurja.data.repository

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import com.gramaurja.data.local.dao.AlertDao
import com.gramaurja.data.local.dao.PowerStatusDao
import com.gramaurja.data.local.dao.PumpHistoryDao
import com.gramaurja.data.local.dao.ReportDao
import com.gramaurja.data.local.dao.UserDao
import com.gramaurja.data.local.dao.ZoneSelectionDao
import com.gramaurja.data.local.entity.AlertEntity
import com.gramaurja.data.local.entity.PowerStatusEntity
import com.gramaurja.data.local.entity.PumpHistoryEntity
import com.gramaurja.data.local.entity.ReportEntity
import com.gramaurja.data.local.entity.UserEntity
import com.gramaurja.data.local.entity.ZoneSelectionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

data class AppRepositories(
    val userRepository: UserRepository,
    val powerRepository: PowerRepository,
    val reportRepository: ReportRepository,
    val pumpRepository: PumpRepository,
    val alertRepository: AlertRepository,
    val zoneRepository: ZoneRepository
)

class UserRepository(private val userDao: UserDao) {
    fun observeUser(): Flow<UserEntity?> = userDao.observeUser()
    suspend fun getStoredUser(): UserEntity? = userDao.getStoredUser()
    suspend fun getUserByPhone(phone: String): UserEntity? = userDao.getUserByPhone(phone)
    suspend fun saveUser(user: UserEntity) = userDao.upsertUser(user)
    suspend fun clearUser() = userDao.clearUser()
}

class PowerRepository(
    private val powerStatusDao: PowerStatusDao,
    private val alertDao: AlertDao,
    private val zoneRepository: ZoneRepository
) {
    fun observeLatestStatus(): Flow<PowerStatusEntity?> = zoneRepository.observeCurrentZone()
        .flatMapLatest { zone ->
            if (zone == null) flowOf(null) else powerStatusDao.observeLatestStatus(zone.zone)
        }

    fun observeRecentStatuses(limit: Int = 10): Flow<List<PowerStatusEntity>> =
        zoneRepository.observeCurrentZone().flatMapLatest { zone ->
            if (zone == null) flowOf(emptyList()) else powerStatusDao.observeRecentStatuses(zone.zone, limit)
        }

    fun observeAllStatuses(): Flow<List<PowerStatusEntity>> = zoneRepository.observeCurrentZone()
        .flatMapLatest { zone ->
            if (zone == null) flowOf(emptyList()) else powerStatusDao.observeAllStatuses(zone.zone)
        }

    fun observeActiveZones(): Flow<List<String>> = powerStatusDao.observeLatestStatusesAcrossZones()
        .map { statuses ->
            statuses
                .filter { it.status == "ON" }
                .map { it.zone }
        }

    suspend fun updatePower(status: String, zone: String, timestamp: Long) {
        powerStatusDao.insertStatus(
            PowerStatusEntity(
                status = status,
                zone = zone,
                timestamp = timestamp
            )
        )
        alertDao.insertAlert(
            AlertEntity(
                message = "Power turned $status in $zone",
                timestamp = timestamp
            )
        )
    }
}

class ReportRepository(private val reportDao: ReportDao) {
    fun observeReports(): Flow<List<ReportEntity>> = reportDao.observeReports()

    suspend fun submitReport(issueType: String, description: String, imageUri: String, timestamp: Long) {
        reportDao.insertReport(
            ReportEntity(
                issueType = issueType,
                description = description,
                imageUri = imageUri,
                timestamp = timestamp
            )
        )
    }
}

class PumpRepository(private val pumpHistoryDao: PumpHistoryDao) {
    fun observeHistory(): Flow<List<PumpHistoryEntity>> = pumpHistoryDao.observePumpHistory()

    suspend fun saveCalculation(cropType: String, duration: String, timestamp: Long) {
        pumpHistoryDao.insertPumpHistory(
            PumpHistoryEntity(
                cropType = cropType,
                duration = duration,
                timestamp = timestamp
            )
        )
    }
}

class AlertRepository(
    private val alertDao: AlertDao,
    private val powerStatusDao: PowerStatusDao,
    private val zoneRepository: ZoneRepository
) {
    fun observeAlerts(): Flow<List<AlertEntity>> = alertDao.observeAlerts()
    fun observePowerUpdates(): Flow<List<PowerStatusEntity>> = zoneRepository.observeCurrentZone()
        .flatMapLatest { zone ->
            if (zone == null) flowOf(emptyList()) else powerStatusDao.observeRecentStatuses(zone.zone, 20)
        }

    suspend fun pushMessage(message: String, timestamp: Long) {
        alertDao.insertAlert(AlertEntity(message = message, timestamp = timestamp))
    }
}

class ZoneRepository(private val zoneSelectionDao: ZoneSelectionDao) {
    fun observeCurrentZone(): Flow<ZoneSelectionEntity?> = zoneSelectionDao.observeCurrentZone()
    fun observeZoneHistory(): Flow<List<ZoneSelectionEntity>> = zoneSelectionDao.observeZoneHistory()

    suspend fun selectZone(district: String, village: String, zone: String, timestamp: Long) {
        zoneSelectionDao.clearActiveSelections()
        zoneSelectionDao.insertSelection(
            ZoneSelectionEntity(
                district = district,
                village = village,
                zone = zone,
                selectedAt = timestamp,
                isActive = true
            )
        )
    }

    suspend fun clearSelections() = zoneSelectionDao.clearAll()
}

private val Context.dataStore by preferencesDataStore(name = "grama_urja_preferences")

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val language = stringPreferencesKey("language")
        val darkTheme = booleanPreferencesKey("dark_theme")
        val signedIn = booleanPreferencesKey("signed_in")
    }

    val selectedLanguage: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Keys.language] ?: ""
    }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.darkTheme] ?: false
    }

    val isSignedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.signedIn] ?: false
    }

    suspend fun saveLanguage(code: String) {
        context.dataStore.edit { it[Keys.language] = code }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[Keys.darkTheme] = enabled }
    }

    suspend fun setSignedIn(enabled: Boolean) {
        context.dataStore.edit { it[Keys.signedIn] = enabled }
    }
}
