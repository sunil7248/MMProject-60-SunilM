package com.gramaurja.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gramaurja.data.local.entity.AlertEntity
import com.gramaurja.data.local.entity.PowerStatusEntity
import com.gramaurja.data.local.entity.PumpHistoryEntity
import com.gramaurja.data.local.entity.ReportEntity
import com.gramaurja.data.local.entity.UserEntity
import com.gramaurja.data.local.entity.ZoneSelectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun observeUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getStoredUser(): UserEntity?

    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    suspend fun getUserByPhone(phone: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearUser()
}

@Dao
interface PowerStatusDao {
    @Query("SELECT * FROM power_status WHERE zone = :zone ORDER BY timestamp DESC LIMIT 1")
    fun observeLatestStatus(zone: String): Flow<PowerStatusEntity?>

    @Query("SELECT * FROM power_status WHERE zone = :zone ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecentStatuses(zone: String, limit: Int): Flow<List<PowerStatusEntity>>

    @Query("SELECT * FROM power_status WHERE zone = :zone ORDER BY timestamp DESC")
    fun observeAllStatuses(zone: String): Flow<List<PowerStatusEntity>>

    @Query(
        """
        SELECT * FROM power_status
        WHERE id IN (
            SELECT MAX(latest.id)
            FROM power_status AS latest
            GROUP BY latest.zone
        )
        ORDER BY zone ASC
        """
    )
    fun observeLatestStatusesAcrossZones(): Flow<List<PowerStatusEntity>>

    @Insert
    suspend fun insertStatus(status: PowerStatusEntity)
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun observeReports(): Flow<List<ReportEntity>>

    @Insert
    suspend fun insertReport(report: ReportEntity)
}

@Dao
interface PumpHistoryDao {
    @Query("SELECT * FROM pump_history ORDER BY timestamp DESC")
    fun observePumpHistory(): Flow<List<PumpHistoryEntity>>

    @Insert
    suspend fun insertPumpHistory(history: PumpHistoryEntity)
}

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts ORDER BY timestamp DESC")
    fun observeAlerts(): Flow<List<AlertEntity>>

    @Insert
    suspend fun insertAlert(alert: AlertEntity)
}

@Dao
interface ZoneSelectionDao {
    @Query("SELECT * FROM zone_selections WHERE isActive = 1 ORDER BY selectedAt DESC LIMIT 1")
    fun observeCurrentZone(): Flow<ZoneSelectionEntity?>

    @Query("SELECT * FROM zone_selections ORDER BY selectedAt DESC")
    fun observeZoneHistory(): Flow<List<ZoneSelectionEntity>>

    @Query("UPDATE zone_selections SET isActive = 0")
    suspend fun clearActiveSelections()

    @Insert
    suspend fun insertSelection(selection: ZoneSelectionEntity)

    @Query("DELETE FROM zone_selections")
    suspend fun clearAll()
}
