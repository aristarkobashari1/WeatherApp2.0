package com.example.model

data class HourlyWeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: ArrayList<CurrentWeatherResponse>
)
