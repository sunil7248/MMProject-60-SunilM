package com.gramaurja

import android.app.Application
import com.gramaurja.data.local.GramaUrjaDatabase
import com.gramaurja.data.repository.AlertRepository
import com.gramaurja.data.repository.AppRepositories
import com.gramaurja.data.repository.PowerRepository
import com.gramaurja.data.repository.PumpRepository
import com.gramaurja.data.repository.ReportRepository
import com.gramaurja.data.repository.UserPreferencesRepository
import com.gramaurja.data.repository.UserRepository
import com.gramaurja.data.repository.ZoneRepository

class GramaUrjaApplication : Application() {
    val database by lazy { GramaUrjaDatabase.build(this) }

    val preferencesRepository by lazy { UserPreferencesRepository(this) }

    val repositories by lazy {
        val zoneRepository = ZoneRepository(database.zoneSelectionDao())
        AppRepositories(
            userRepository = UserRepository(database.userDao()),
            powerRepository = PowerRepository(database.powerStatusDao(), database.alertDao(), zoneRepository),
            reportRepository = ReportRepository(database.reportDao()),
            pumpRepository = PumpRepository(database.pumpHistoryDao()),
            alertRepository = AlertRepository(database.alertDao(), database.powerStatusDao(), zoneRepository),
            zoneRepository = zoneRepository
        )
    }
}
