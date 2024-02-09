package com.example.feature.data.repo_impl


import com.example.data.model.mapToWeatherEntity
import com.example.data.repository.WeatherRepository
import com.example.database.dao.WeatherDao
import com.example.database.entity.Weather
import com.example.feature.data.fakes.responses.CurrentWeatherFakeResponse
import com.example.feature.data.fakes.responses.HourlyWeatherFakeResponse
import com.example.model.Coord
import com.example.model.CurrentWeatherResponse
import com.example.model.HourlyWeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


class FakeWeatherRepositoryImpl(
    private val weatherDao: WeatherDao
) : WeatherRepository {

    lateinit var currentWeatherResponseTest: Weather
    lateinit var hourlyWeatherResponseTest: HourlyWeatherResponse
    lateinit var weeklyWeatherResponseTest: List<Weather>

    override fun getCurrentWeather(coord: Coord,lang:String,unit:String) = flow {
        currentWeatherResponseTest = Gson().fromJson(
            CurrentWeatherFakeResponse.fakeJsonResponse,
            CurrentWeatherResponse::class.java
        ).mapToWeatherEntity()
        emit(currentWeatherResponseTest)
    }

    override fun getHourlyWeather(coord: Coord,lang:String,unit:String) = flow {
        hourlyWeatherResponseTest = Gson().fromJson(
            HourlyWeatherFakeResponse.fakeJsonResponse,
            HourlyWeatherResponse::class.java
        )
        emit(hourlyWeatherResponseTest)
    }

    override fun getWeeklyWeather(coord: Coord,lang:String,unit:String) = flow {
        val currentWeatherResponse = Gson().fromJson(
            CurrentWeatherFakeResponse.fakeJsonResponse,
            CurrentWeatherResponse::class.java
        )
        weeklyWeatherResponseTest = List(7) { currentWeatherResponse.mapToWeatherEntity() }
        emit(weeklyWeatherResponseTest)
    }

    override fun getSearchedCity(coord: Coord,lang:String,unit:String) = flow {
        currentWeatherResponseTest = Gson().fromJson(
            CurrentWeatherFakeResponse.fakeJsonResponse,
            CurrentWeatherResponse::class.java
        ).mapToWeatherEntity()

        if (!doesWeatherExist(currentWeatherResponseTest.location!!).first())
            insertWeatherToDB(currentWeatherResponseTest)

        emit(currentWeatherResponseTest)
    }

    override suspend fun insertWeatherToDB(weather: Weather) = weatherDao.insertEntity(weather)


    override fun getWeatherListFromDB() = weatherDao.getAllWeather()

    override fun getWeather(coord: Coord) =
        weatherDao.getWeather(coord.lat.toString(), coord.lon.toString())

    override fun doesWeatherExist(location: String) = weatherDao.doesWeatherExist(location)

    override fun getCity(coord: Coord) = flow {
        emit(getWeather(coord).first().location?:"No city founded")
    }
}