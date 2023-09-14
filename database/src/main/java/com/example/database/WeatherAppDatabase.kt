package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.dao.WeatherDao
import com.example.database.entity.Weather

@Database(entities = [Weather::class], version = 1)
abstract class WeatherAppDatabase:RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao
}