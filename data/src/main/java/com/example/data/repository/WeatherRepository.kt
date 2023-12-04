package com.example.data.repository

import com.example.model.Coord
import com.example.database.entity.Weather
import com.example.model.HourlyWeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getCurrentWeather(coord: Coord,lang:String,unit:String): Flow<Weather>

    fun getHourlyWeather(coord: Coord,lang:String,unit:String): Flow<HourlyWeatherResponse>

    fun getWeeklyWeather(coord: Coord,lang:String,unit:String): Flow<List<Weather>>

    fun getSearchedCity(coord: Coord,lang:String,unit:String): Flow<Weather?>

    suspend fun insertWeatherToDB(weather: Weather)

    fun getWeatherListFromDB(): Flow<List<Weather>>

    fun getWeather(coord: Coord): Flow<Weather>

    fun doesWeatherExist(location:String): Flow<Boolean>

    fun getCity(coord: Coord): Flow<String>

}