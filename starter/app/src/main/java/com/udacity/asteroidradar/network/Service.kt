package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * Retrofit service to fetch the list of Near Earth Asteroids
 */
interface NasaApiService {
    @GET(value = "neo/rest/v1")
    fun getNearEarthObjectsAsync() : Deferred<NetworkNearEarthObjectContainer>
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
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val nasaApi = retrofit.create(NasaApiService::class.java)
}