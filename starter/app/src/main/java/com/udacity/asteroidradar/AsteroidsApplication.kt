package com.udacity.asteroidradar

import android.app.Application
import timber.log.Timber

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