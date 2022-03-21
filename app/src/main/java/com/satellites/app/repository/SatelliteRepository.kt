package com.satellites.app.repository

import androidx.annotation.WorkerThread
import com.satellites.app.dao.SatelliteDAO
import com.satellites.app.entities.*
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class SatelliteRepository(private val satelliteDAO: SatelliteDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allSatellites: Flow<List<Satellite>> = satelliteDAO.getAllSatellites()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSatellite(satellite: Satellite) {
        satelliteDAO.insertSatellite(satellite)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertDetails(details: Details) {
        satelliteDAO.insertDetails(details)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPosition(position: Position) {
        satelliteDAO.insertPosition(position)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteSatellite(satellite: Satellite) {
        satelliteDAO.delete(satellite)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getSatelliteDetails(id: Int): List<SatelliteDetails> {
      return satelliteDAO.getSatelliteDetails(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getDetailsBySatelliteId(id: Int): Details? {
        return satelliteDAO.getDetailsBySatelliteId(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getAllPositionsBySatelliteId(id: Int): List<Position> {
        return satelliteDAO.getAllPositionsBySatelliteId(id)
    }
}