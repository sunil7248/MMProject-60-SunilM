package com.gramaurja.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
    entities = [
        UserEntity::class,
        PowerStatusEntity::class,
        ReportEntity::class,
        PumpHistoryEntity::class,
        AlertEntity::class,
        ZoneSelectionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class GramaUrjaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun powerStatusDao(): PowerStatusDao
    abstract fun reportDao(): ReportDao
    abstract fun pumpHistoryDao(): PumpHistoryDao
    abstract fun alertDao(): AlertDao
    abstract fun zoneSelectionDao(): ZoneSelectionDao

    companion object {
        fun build(context: Context): GramaUrjaDatabase =
            Room.databaseBuilder(
                context,
                GramaUrjaDatabase::class.java,
                "grama_urja.db"
            ).fallbackToDestructiveMigration().build()
    }
}
