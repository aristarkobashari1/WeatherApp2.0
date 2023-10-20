package com.example.feature.epoxy

import com.example.database.entity.Weather
import com.example.feature.CurrentWeatherTodayForecastBindingModel_

fun List<Weather>.modelToEpoxy(tempUnit:String): List<CurrentWeatherTodayForecastBindingModel_> {
    return map {
        CurrentWeatherTodayForecastBindingModel_()
            .id(it.id) //or UUID.randomUUID().toString()
            .weather(it)
            .tempUnit(tempUnit)
    }
}