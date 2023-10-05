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

    private val lang = "al" //later save them to shared pref
    private val units = "metric"

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

    override fun getSearchedCity(coord: Coord) = flow {
            val weather = weatherService.getCurrentWeather(
                coord.lat,
                coord.lon,
                lang,
                units
            ).mapToWeatherEntity()

            if (!doesWeatherExist(weather.location!!).first())
                insertWeatherToDB(weather)


            emit(weather)
    }

    override suspend fun insertWeatherToDB(weather: Weather) = weatherDao.insertEntity(weather)

    override fun getWeatherListFromDB() = weatherDao.getAllWeather()
    override fun getWeather(coord: Coord): Flow<Weather> = weatherDao.getWeather(coord.lat.toString(),coord.lon.toString())

    override fun doesWeatherExist(location: String) = weatherDao.doesWeatherExist(location)

    override fun getCity(coord: Coord) = flow {
        emit(getWeather(coord).first().location?:"No city founded")
    }

}

