package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.NearEarthObject
import timber.log.Timber

/**
 * Database object for Near Earth Objects
 */
@Entity
data class DatabaseNearEarthObjects(
    @PrimaryKey
    val id: Long,
    val absolute_magnitude: Double,
    val estimated_max_diameter: Double,
    val potentially_hazardous_flg: Boolean,
    val kilometers_per_second: Double,
    val astronomical: Double
)

/**
 * Returns a list of [NearEarthObject] converted from the database
 * objects [DatabaseNearEarthObjects]
 */
fun List<DatabaseNearEarthObjects>.asDomainModel() : List<NearEarthObject> {
    Timber.d("Converting database objects to domain objects")
    return map {
        NearEarthObject(
            id = it.id,
            absoluteMagnitude = it.absolute_magnitude,
            estimatedDiameterMax = it.estimated_max_diameter,
            isPotentiallyHazardousAsteroid = it.potentially_hazardous_flg,
            kilometersPerSecond = it.kilometers_per_second,
            astronomical = it.astronomical
        )
    }

}