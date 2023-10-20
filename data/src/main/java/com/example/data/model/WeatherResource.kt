package com.example.data.model

import com.example.common.getClockFromTimestamp
import com.example.common.getDayFromTimestamp
import com.example.database.entity.Weather
import com.example.model.CurrentWeatherResponse

fun CurrentWeatherResponse.mapToWeatherEntity(): Weather {
        return Weather(
        description = this.weather?.first()?.main?:"No description",
        detailDescription = this.weather?.first()?.description?:"No detailed description",
        temperature = this.main?.temp?.toString()?:"No temperature",
        rain = this.rain?.`1h`?.toString()?: "No rain",
        windSpeed = this.wind?.speed?.toString()?: "No wind",
        tempMax = this.main?.temp_max?.toString()?:"No temp max",
        tempMin = this.main?.temp_min?.toString()?:"No temp min",
        time = this?.dt?.getDayFromTimestamp() ?:"No data provided",
        clock = this?.dt?.getClockFromTimestamp() ?: "No data provided",
        location = this?.name ?:"No location provided",
        latitude = this.coord?.lat?.toString()?: "No latitude",
        longitude = this.coord?.lon?.toString()?: "No longitude",
        iconCode = this.weather.firstOrNull()?.icon?: "No icon"
    )
}

fun List<CurrentWeatherResponse>.mapToWeatherEntity(): List<Weather>{
        val weatherList = mutableListOf<Weather>()
        forEach { currentWeatherResponse ->
                weatherList.add(currentWeatherResponse.mapToWeatherEntity())
        }
        return weatherList
}