package com.example.data.model

import com.example.core_model.Clouds
import com.example.core_model.Main
import com.example.core_model.Rain
import com.example.core_model.Sys
import com.example.core_model.Weather
import com.example.core_model.Wind
import com.example.model.Coord
import com.example.model.CurrentWeatherResponse
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class WeatherResourceKtTest{

    private lateinit var currWeatherResponse: CurrentWeatherResponse
    private lateinit var currentWeatherResponseList: ArrayList<CurrentWeatherResponse>

    @Before
    fun setUp(){
          currWeatherResponse = CurrentWeatherResponse(
            coord = Coord(10.99, 44.34),
            weather = listOf(Weather(id = 501, main = "Rain", description = "moderate rain", icon =  "10d")),
            base="stations",
            main = Main(temp = 298.48, feels_like = 298.74, temp_min = 297.56, temp_max = 300.05, pressure = 1015, humidity = 64, sea_level = 1015, grnd_level = 933),
            visibility = 10000,
            wind = Wind(speed = 0.62, deg =  349, gust = 1.18),
            rain = Rain(3.16),
            clouds = Clouds(100),
            dt = 1661870592,
            sys = Sys(type = 2, id = 2075663, country = "IT", sunrise = 1661834187, sunset = 1661882248),
            timezone = 7200,
            id = 3163858,
            name = "Zocca",
            cod = 200
        )
        currentWeatherResponseList = arrayListOf(currWeatherResponse,currWeatherResponse)

    }



    @Test
    fun currentWeatherResponse_mapped_to_DAO_entity(){
        val weatherEntity =  currWeatherResponse.mapToWeatherEntity()
        assertThat("Rain").isEqualTo(weatherEntity.description)
        assertThat("Zocca").isEqualTo(weatherEntity.location)
    }


    @Test
    fun currentWeatherReponseList_mapped_to_DAO_entity(){
        val weatherEntityList = currentWeatherResponseList.mapToWeatherEntity()
        weatherEntityList.forEach { weatherEntity->
            assertThat("Rain").isEqualTo(weatherEntity.description)
            assertThat("Zocca").isEqualTo(weatherEntity.location)
        }
    }
}