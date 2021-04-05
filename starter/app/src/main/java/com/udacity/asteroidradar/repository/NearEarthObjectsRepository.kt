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
            // Try statement is used to catch Network Exceptions so the app does not
            // crash when attempting to load without a network connection
            try {
                Timber.d("Changed to IO Scope to run request")
                val todaysDate = Utils.convertDateStringToFormattedString(Calendar.getInstance().time, Constants.API_QUERY_DATE_FORMAT)
                val nearEarthObjects = Network.nasaApi.getNearEarthObjectsAsync(Constants.API_KEY, todaysDate).await()
                Timber.d("Retrieved Near Earth Objects from NASA API. Parsing JSON response.")
                database.nearEarthObjectDao.insertAllNearEarthObjects(*parseAsteroidsJsonResult(JSONObject(nearEarthObjects)).asDatabaseModel())
                Timber.d("Exiting IO Scope.")
            } catch (e: Exception) {
                Timber.e(e)
            }

        }
    }
}