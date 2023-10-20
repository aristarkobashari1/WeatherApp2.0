package com.example.network.services

import com.example.common.Api
import com.example.model.CurrentWeatherResponse
import com.example.model.HourlyWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("lang") language: String,
        @Query("units") units: String,
        @Query("appid") appId: String = Api.API_KEY,
        ): CurrentWeatherResponse


    @GET("forecast")
    suspend fun getHourlyWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("lang") language: String = "sq",
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = Api.API_KEY
    ): HourlyWeatherResponse
}

