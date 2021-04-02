package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.NearEarthObjectsRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalArgumentException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val nearEarthObjectsRepository = NearEarthObjectsRepository(database)

    val asteroids = nearEarthObjectsRepository.nearEarthObjects

    init {
        Timber.d("init() MainViewModel")
        viewModelScope.launch {
            nearEarthObjectsRepository.refreshNearEarthObjects()
        }
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
}