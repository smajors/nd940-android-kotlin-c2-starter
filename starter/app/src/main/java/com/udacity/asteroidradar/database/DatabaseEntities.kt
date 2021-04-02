package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.NearEarthObject
import timber.log.Timber

/**
 * Database object for Near Earth Objects
 */
@Entity
data class DatabaseNearEarthObjects constructor(
    @PrimaryKey
    val id: Long = -1L,
    val code_name: String,
    val date: String,
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
            codeName = it.code_name,
            date = it.date,
            absoluteMagnitude = it.absolute_magnitude,
            estimatedDiameterMax = it.estimated_max_diameter,
            isPotentiallyHazardousAsteroid = it.potentially_hazardous_flg,
            kilometersPerSecond = it.kilometers_per_second,
            astronomical = it.astronomical
        )
    }

}

fun List<NearEarthObject>.asDatabaseModel(): Array<DatabaseNearEarthObjects> {
    return map {
        DatabaseNearEarthObjects (
                id = it.id,
                code_name = it.codeName,
                date = it.date,
                absolute_magnitude = it.absoluteMagnitude,
                estimated_max_diameter = it.estimatedDiameterMax,
                kilometers_per_second = it.kilometersPerSecond,
                astronomical = it.astronomical,
                potentially_hazardous_flg = it.isPotentiallyHazardousAsteroid
        )
    }.toTypedArray()
}