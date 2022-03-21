package com.satellites.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.satellites.app.entities.Details
import com.satellites.app.entities.Position
import com.satellites.app.entities.Satellite
import com.satellites.app.dao.SatelliteDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Satellite::class, Details::class, Position::class],
    version = 1,
    exportSchema = false
)
abstract class SatelliteDatabase : RoomDatabase() {

    abstract fun satelliteDAO(): SatelliteDAO

    private class SatelliteDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val satelliteDAO = database.satelliteDAO()

                    // Delete all content here.
                    satelliteDAO.deleteAll()

                }
            }
        }
    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: SatelliteDatabase? = null

        @Synchronized
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): SatelliteDatabase {

            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context, SatelliteDatabase::class.java,
                    "satellite_database"
                ).allowMainThreadQueries().addCallback(SatelliteDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }

        }
    }


}