package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Utils
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.time.LocalDate
import java.util.*

class PictureOfTheDayRepository(private val database: NasaDatabase) {
    // LiveData for Picture of the day
    val pictureOfTheDay: LiveData<PictureOfDay> = Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
        it?.asDomainModel()
    }
    /**
     * Refreshes picture of the day from the Nasa Datasource API
     */
    suspend fun refreshPictureOfTheDay() {
        Timber.d("Refreshing Picture of the Day from NASA API request")
        // Run this request in the IO Context
        withContext(Dispatchers.IO) {
            // Try statement is used to catch Network Exceptions so the app does not
            // crash when attempting to load without a network connection
            try {
                Timber.d("Context changed to IO Scope to run request")
                val todaysDate = Utils.convertDateStringToFormattedString(Calendar.getInstance().time, Constants.API_QUERY_DATE_FORMAT)
                val pictureOfTheDay = Network.nasaApi.getPictureOfTheDay(Constants.API_KEY, todaysDate).await()
                Timber.d("Retrieved Picture of the Day from NASA API")
                database.pictureOfDayDao.insertPictureOfDay(pictureOfTheDay.asDatabaseModel())
                Timber.d("Inserted record into database")
                Timber.d("Exiting IO Scope.")
            } catch (e: Exception) {
                Timber.e(e)
            }

        }
    }
}