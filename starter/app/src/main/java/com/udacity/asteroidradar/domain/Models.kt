package com.udacity.asteroidradar.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Domain objects are objects that are used by the application directly
*/

/**
 * Represents a Near Earth Object
 */
@Parcelize
data class NearEarthObject(
    val id: Long,
    val codeName: String,
    val date: String,
    val absoluteMagnitude: Double,
    val estimatedDiameterMax: Double,
    val kilometersPerSecond: Double,
    val astronomical: Double,
    val isPotentiallyHazardousAsteroid: Boolean


) : Parcelable