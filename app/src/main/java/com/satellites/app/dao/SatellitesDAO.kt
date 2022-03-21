package com.satellites.app.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.satellites.app.entities.*
import kotlinx.coroutines.flow.Flow
@Dao
interface SatelliteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSatellite(satellite: Satellite)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPosition(position: Position)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDetails(details: Details)

    @Update
    fun update(satellite: Satellite)

    @Delete
    fun delete(satellite: Satellite)

    @Query("delete from satellites")
    suspend fun deleteAll()

    @Query("SELECT  * FROM satellites")
    fun getAllSatellitesLiveData(): LiveData<List<Satellite>>

    @Query("SELECT * FROM satellites")
    fun getAllSatellites(): Flow<List<Satellite>>

    @Transaction
    @Query("SELECT * FROM satellites Where id = :id")
    fun getSatelliteDetails(id:Int): List<SatelliteDetails>

    @Transaction
    @Query("SELECT * FROM details Where id = :id LIMIT 1")
    fun getDetailsBySatelliteId(id:Int): Details?


    @Query("SELECT * FROM positions Where id = :id")
    fun getAllPositionsBySatelliteId(id:Int?): List<Position>

//    @Query("SELECT * FROM satellites LIMIT 1")
//    fun getLastSatellite(): Satellite?

}