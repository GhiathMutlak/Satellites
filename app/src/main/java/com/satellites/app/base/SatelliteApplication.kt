package com.satellites.app.base

import android.app.Application
import com.satellites.app.repository.SatelliteRepository
import com.satellites.app.database.SatelliteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class SatelliteApplication : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { SatelliteDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { SatelliteRepository(database.satelliteDAO()) }
}