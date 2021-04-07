package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service to fetch the list of Near Earth Asteroids
 */
interface NasaApiService {
    /**
     * Endpoint for Near earth objects
     */
    @GET(value = "neo/rest/v1/feed?api_key=${Constants.API_KEY}")
    fun getNearEarthObjectsAsync() : Deferred<String>

    /**
     * Endpoint for Picture of the day
     */
    @GET(value = "planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String,
            @Query("date") date: String): Deferred<PictureOfDay>
}

/**
 * Moshi instance
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Entry point for Network access.
 */
object Network {
    private val retrofit = Retrofit.Builder()
        .baseUrl(
            Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val nasaApi = retrofit.create(NasaApiService::class.java)
}