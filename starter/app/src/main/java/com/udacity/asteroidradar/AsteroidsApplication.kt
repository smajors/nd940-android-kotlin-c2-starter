package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.repository.RefreshAsteroidsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Provides the main entry point of the application to set up long running tasks.
 * Also used to set up the main logging used by this application.
 */
class AsteroidsApplication : Application() {
    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        // Plant LineNumberDebugTree
        Timber.plant(LineNumberDebugTree())
        Timber.d("Timber logging initialized.")
        // Run long running startup tasks
        deferredInit()
    }

    /**
     * Initialization for long running deferred start items are ran in this routine
     */
    private fun deferredInit() = CoroutineScope(Dispatchers.Default).launch {
        // Recurring work setup
        Timber.d("Creating daily work request")
        val workConstraints = Constraints.Builder()
                // Use only unmetered networks
                .setRequiredNetworkType(NetworkType.UNMETERED)
                // Use only if the charge state is not low
                .setRequiresBatteryNotLow(true)
                // Always use if charging the device
                .setRequiresCharging(true)
                .apply {
                    // If this device is Marshmallow or higher, require device idle
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }.build()

        // Create a workrequest that runs once a day
        val request = PeriodicWorkRequestBuilder<RefreshAsteroidsWorker>(RefreshAsteroidsWorker.TIMES_PER_DAY, TimeUnit.DAYS)
                .setConstraints(workConstraints)
                .build()

        // Set this request to be ran
        WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefreshAsteroidsWorker.WORK_NAME,
                // Should be no conflicts with naming, since this app is the only one to use this request
                ExistingPeriodicWorkPolicy.KEEP,
                request
        )
        Timber.d("Worker request for RefreshAsteroidsWorker created.")
    }

    /**
     * Main class that is used by this application's Timber logging
     * Logs in the format:
     *
     * Level/(Filename.kt:LineNumber)#function: LogString
     */
    class LineNumberDebugTree : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String? {
            return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
        }
    }
}