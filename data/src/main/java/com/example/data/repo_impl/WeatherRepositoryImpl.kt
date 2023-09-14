package com.example.data.repo_impl

import com.example.model.Coord
import com.example.data.model.mapToWeatherEntity
import com.example.data.repository.WeatherRepository
import com.example.database.dao.WeatherDao
import com.example.database.entity.Weather
import com.example.network.services.WeatherService
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    val lang = "al" //later save them to shared pref
    val units = "metric"

    override fun getCurrentWeather(coord: Coord) = flow {
        emit(weatherService.getCurrentWeather(coord.lat, coord.lon, lang, units).mapToWeatherEntity())
    }

    override fun getHourlyWeather(coord: Coord) = flow {
        emit(weatherService.getHourlyWeather(coord.lat, coord.lon, lang, units))
    }

    override fun getWeeklyWeather(coord: Coord) = flow {
        val list = mutableListOf<Weather>()
        repeat(10) {
            list.add(weatherService.getCurrentWeather(coord.lat, coord.lon, lang, units).mapToWeatherEntity())
        }
        emit(list)
    }

    override fun getSearchedCity(city: String) = flow {
        val exists = cities.containsKey(city)
        if (exists) {
            val weather = weatherService.getCurrentWeather(
                cities[city]!!.lat,
                cities[city]!!.lon,
                lang,
                units
            ).mapToWeatherEntity()

            if (!doesWeatherExist(city).first()) {
                insertWeatherToDB(weather)
            }

            emit(weather)

        } else emit(null)

    }

    override suspend fun insertWeatherToDB(weather: Weather) = weatherDao.insertEntity(weather)

    override fun getWeatherListFromDB() = weatherDao.getAllWeather()

    override fun doesWeatherExist(city: String) = weatherDao.doesWeatherExist(city)

    override fun getCity(coord: Coord) = flow {
        emit (cities.filterValues{ it==coord }.keys.first())
    }

}


val cities = mapOf(
    "tokyo" to Coord(35.6895, 139.6917),
    "delhi" to Coord(28.7041, 77.1025),
    "shanghai" to Coord(31.2304, 121.4737),
    "sao Paulo" to Coord(-23.5505, -46.6333),
    "mumbai" to Coord(19.0760, 72.8777),
    "mexico city" to Coord(19.4326, -99.1332),
    "beijing" to Coord(39.9042, 116.4074),
    "osaka" to Coord(34.6937, 135.5023),
    "cairo" to Coord(30.0444, 31.2357),
    "new york" to Coord(40.7128, -74.0060),
    "Globe" to Coord(0.0,0.0)
)