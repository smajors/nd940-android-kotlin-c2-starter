package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import timber.log.Timber
import java.lang.Exception

/**
 * Worker class that will run in the background and update the list of asteroids for today
 */
class RefreshAsteroidsWorker (appContext: Context, params: WorkerParameters)
    : CoroutineWorker(appContext, params) {

    companion object {
        // Unique name for the worker
        const val WORK_NAME = "RefreshAsteroidWorker"
        // Times per day to run worker
        const val TIMES_PER_DAY = 1L
    }

    override suspend fun doWork(): Result {
        Timber.d("Retrieving Asteroids on Worker Thread")
        val database = getDatabase(applicationContext)
        val repository = NearEarthObjectsRepository(database)
        return try {
            repository.refreshNearEarthObjects()
            Timber.d("Repository refreshed successfully.")
            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.retry()
        }
    }
}