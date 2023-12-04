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



    override fun getCurrentWeather(coord: Coord,lang:String,unit:String) = flow {
        emit(weatherService.getCurrentWeather(coord.lat, coord.lon, lang, unit).mapToWeatherEntity())
    }

    override fun getHourlyWeather(coord: Coord,lang:String,unit:String) = flow {
        emit(weatherService.getHourlyWeather(coord.lat, coord.lon, lang, unit))
    }

    override fun getWeeklyWeather(coord: Coord,lang:String,unit:String) = flow {
        val list = mutableListOf<Weather>()
        repeat(7) {
            list.add(weatherService.getCurrentWeather(coord.lat, coord.lon, lang, unit).mapToWeatherEntity())
        }
        emit(list)
    }

    override fun getSearchedCity(coord: Coord,lang:String,unit:String) = flow {
            val weather = weatherService.getCurrentWeather(
                coord.lat,
                coord.lon,
                lang,
                unit
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

