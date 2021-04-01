package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.NearEarthObject
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

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
            val nearEarthObjects = Network.nasaApi.getNearEarthObjectsAsync().await()
            Timber.d("Retrieved Near Earth Objects from NASA API. Caching into database.")
            database.nearEarthObjectDao.insertAllNearEarthObjects(*nearEarthObjects.asDatabaseModel())
            Timber.d("Exiting IO Scope.")
        }
    }
}