package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Utils
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.DatabaseNearEarthObjects
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.NearEarthObject
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.util.*

class NearEarthObjectsRepository(private val database: NasaDatabase) {
    // LiveData for all Near earth objects
    val nearEarthObjects: LiveData<List<NearEarthObject>> = Transformations.map(database.nearEarthObjectDao.getAllNearEarthObjects()) {
        it.asDomainModel()
    }

    /**
     * Refreshes all near earth objects from remote source
     */
    suspend fun refreshNearEarthObjects() {
        Timber.d("Refreshing all Near Earth Objects from NASA API request")
        // Run this request in the IO Context
        withContext(Dispatchers.IO) {
            Timber.d("Changed to IO Scope to run request")
            val todaysDate = Utils.convertDateStringToFormattedString(Calendar.getInstance().time, Constants.API_QUERY_DATE_FORMAT)
            val nearEarthObjects = Network.nasaApi.getNearEarthObjectsAsync(Constants.API_KEY, todaysDate).await()
            Timber.d("Retrieved Near Earth Objects from NASA API. Parsing JSON response.")
            val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(nearEarthObjects))
            val asteroidList = mutableListOf<DatabaseNearEarthObjects>()

            for (asteroid in parsedAsteroids) {
                val dbAsteroid = DatabaseNearEarthObjects(
                        asteroid.id,
                        asteroid.codeName,
                        asteroid.date,
                        asteroid.absoluteMagnitude,
                        asteroid.estimatedDiameterMax,
                        asteroid.isPotentiallyHazardousAsteroid,
                        asteroid.kilometersPerSecond,
                        asteroid.astronomical
                )

                asteroidList.add(dbAsteroid)
            }
            database.nearEarthObjectDao.insertAllNearEarthObjects(*parseAsteroidsJsonResult(JSONObject(nearEarthObjects.toString())).asDatabaseModel())
            Timber.d("Exiting IO Scope.")
        }
    }
}