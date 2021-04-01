package com.udacity.asteroidradar.domain

/**
 * Domain objects are objects that are used by the application directly
*/

/**
 * Represents a Near Earth Object
 */
data class NearEarthObject(
    val id: Long,
    val absoluteMagnitude: Double,
    val estimatedDiameterMax: Double,
    val isPotentiallyHazardousAsteroid: Boolean,
    val kilometersPerSecond: Double,
    val astronomical: Double
)