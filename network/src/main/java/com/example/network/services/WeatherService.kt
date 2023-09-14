package com.example.network.services

import com.example.model.CurrentWeatherResponse
import com.example.model.HourlyWeatherResponse
import org.intellij.lang.annotations.Language
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("lang") language: String,
        @Query("units") units: String,
        @Query("appid") appId: String = APIKEY.API_KEY,
        ): CurrentWeatherResponse


    @GET("forecast")
    suspend fun getHourlyWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("lang") language: String = "sq",
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = APIKEY.API_KEY
    ): HourlyWeatherResponse
}

object APIKEY {
    const val API_KEY = "c18b263ccb35b8c930c109b9edc01349"
}