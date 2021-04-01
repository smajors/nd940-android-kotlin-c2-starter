package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.domain.NearEarthObject
import timber.log.Timber

/**
 * These objects in this file are responsible for the responses directly from the
 * NASA API. These objects should not directly be used by the application,
 * but converted to Domain objects before use
 *
*/

@JsonClass(generateAdapter = true)
data class NetworkNearEarthObjectContainer(val nearEarthObjects: List<NetworkNearEarthObject>)

@JsonClass(generateAdapter = true)
data class NetworkNearEarthObject(
    val id: Long,
    val absolute_magnitude: Double,
    val estimated_diameter_max: Double,
    val is_potentially_hazardous_asteroid: Boolean,
    val kilometers_per_second: Double,
    val astronomical: Double
)

/**
 * Converts Network JSON results to the domain model
 */
fun NetworkNearEarthObjectContainer.asDomainModel() : List<NearEarthObject> {
    Timber.d("Converting Network Objects to Domain Objects")
    return nearEarthObjects.map {
        NearEarthObject(
            id = it.id,
            absoluteMagnitude = it.absolute_magnitude,
            estimatedDiameterMax =  it.estimated_diameter_max,
            isPotentiallyHazardousAsteroid = it.is_potentially_hazardous_asteroid,
            kilometersPerSecond = it.kilometers_per_second,
            astronomical = it.astronomical
        )
    }
}

