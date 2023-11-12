package com.example.model

import com.example.core_model.*

data class CurrentWeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Rain,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
){

    constructor(): this("", Clouds(0),0, Coord(0.0,0.0),0,0, Main(0.0,0,0,0,0,0.0,0.0,0.0)
    ,"", Rain(0.0), Sys("",0,0,0,0),0,0, arrayListOf(),Wind(0,0.0,0.0)
    )
}



