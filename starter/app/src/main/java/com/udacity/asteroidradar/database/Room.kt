package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import timber.log.Timber

/**
 * Static instance of NasaDatabase
 */
private lateinit var INSTANCE: NasaDatabase

@Dao
interface NearEarthObjectDao {
    @Query(NasaDatabase.GET_ALL_NEAR_EARTH_OBJECTS)
    fun getAllNearEarthObjects() : LiveData<List<DatabaseNearEarthObjects>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllNearEarthObjects(vararg nearEarthObjects: DatabaseNearEarthObjects)
}

@Dao
interface PictureOfDayDao {
    @Query(NasaDatabase.GET_PICTURE_OF_DAY)
    fun getPictureOfDay() : LiveData<DatabasePictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(pictureOfDay: DatabasePictureOfDay)
}

@Database(entities = [DatabaseNearEarthObjects::class, DatabasePictureOfDay::class], version = 3)
abstract class NasaDatabase : RoomDatabase() {
    /**
     * All queries are stored in this object
     * All constants related to the database are in this object
     */
    companion object {
        const val DATABASE_NAME = "NasaDatabase"
        const val GET_ALL_NEAR_EARTH_OBJECTS = "select * from databasenearearthobjects order by date desc"
        const val GET_PICTURE_OF_DAY = "SELECT * FROM DatabasePictureOfDay pod  ORDER BY pod.date DESC LIMIT 0,1"
    }

    abstract val nearEarthObjectDao: NearEarthObjectDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

fun getDatabase(context: Context) : NasaDatabase {
    Timber.d("Attempting to retrieve database instance.")
    synchronized(NasaDatabase::class.java) {
        // If the instance does not exist, create it.
        if (!::INSTANCE.isInitialized) {
            Timber.d("Database instance does not exist. Creating database instance.")
            INSTANCE = Room.databaseBuilder(context.applicationContext, NasaDatabase::class.java, NasaDatabase.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            Timber.d("Database instance created.")
        }
    }
    Timber.d("Returning database instance.")
    return INSTANCE
}