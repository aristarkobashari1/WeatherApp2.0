package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entity.Weather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(weather: Weather)

    @Query("SELECT * FROM weather_table")
    fun getAllWeather(): Flow<List<Weather>>

    @Query("SELECT EXISTS (SELECT 1 FROM weather_table WHERE LOWER(location) = LOWER(:city) LIMIT 1)")
    fun doesWeatherExist(city: String): Flow<Boolean>


}