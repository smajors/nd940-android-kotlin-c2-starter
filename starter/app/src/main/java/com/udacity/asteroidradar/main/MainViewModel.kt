package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.NearEarthObject
import com.udacity.asteroidradar.repository.NearEarthObjectsRepository
import com.udacity.asteroidradar.repository.PictureOfTheDayRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalArgumentException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val filter = MutableLiveData(ObjectFilter.WEEKLY)
    private val nearEarthObjectsRepository = NearEarthObjectsRepository(database)
    private val pictureOfTheDayRepository = PictureOfTheDayRepository(database)

    val asteroids = Transformations.switchMap(filter) {
        when (it!!) {
            ObjectFilter.WEEKLY -> nearEarthObjectsRepository.weeklyNearEarthObjects
            ObjectFilter.TODAY -> nearEarthObjectsRepository.todayNearEarthObjects
            else -> nearEarthObjectsRepository.nearEarthObjects
        }
    }
    val pictureOfDay = pictureOfTheDayRepository.pictureOfTheDay

    // Navigation Live Data
    private val _detailNavigation = MutableLiveData<NearEarthObject>()
    val detailNavigation: LiveData<NearEarthObject>
     get() = _detailNavigation

    init {
        Timber.d("init() MainViewModel")
        viewModelScope.launch {
            nearEarthObjectsRepository.refreshNearEarthObjects()
            pictureOfTheDayRepository.refreshPictureOfTheDay()
        }
    }

    /**
     * Global onClickHandler for Asteroid List Items
     */
    fun onObjectClick(asteroid: NearEarthObject) {
        _detailNavigation.value = asteroid
    }

    /**
     * Global function to reset navigation state
     */
    fun onObjectClickNavigation() {
        _detailNavigation.value = null
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            Timber.d("Attempting to cast to MainViewModel in Factory")
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                Timber.d("Cast is able to happen, continuing.")
                return MainViewModel(app) as T
            }

            val e = IllegalArgumentException("FATAL: Unable to cast ViewModel.")
            Timber.e(e, "Error casting ViewModel")
            throw e
        }
    }

    enum class ObjectFilter {
        ALL,
        TODAY,
        WEEKLY
    }

    fun setFilter(objectFilter: ObjectFilter) {
        filter.postValue((objectFilter))
    }

}