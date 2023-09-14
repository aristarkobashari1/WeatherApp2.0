package com.example.data.repository

import com.example.model.Coord
import com.example.database.entity.Weather
import com.example.model.HourlyWeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getCurrentWeather(coord: Coord): Flow<Weather>

    fun getHourlyWeather(coord: Coord): Flow<HourlyWeatherResponse>

    fun getWeeklyWeather(coord: Coord): Flow<List<Weather>>

    fun getSearchedCity(city: String): Flow<Weather?>

    suspend fun insertWeatherToDB(weather: Weather)

    fun getWeatherListFromDB(): Flow<List<Weather>>

    fun doesWeatherExist(city:String): Flow<Boolean>

    fun getCity(coord: Coord): Flow<String>

}