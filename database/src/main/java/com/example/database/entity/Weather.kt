package com.example.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String? = "",
    val detailDescription: String? = "",
    val temperature: String? = "",
    val rain: String? = "",
    val windSpeed: String? = "",
    val tempMax: String? = "",
    val tempMin: String? = "",
    val time: String? = "",
    val clock: String? = "",
    val location: String? = "",
    val latitude: String? = "",
    val longitude: String? = ""
)

fun Weather.nothingFound(): Boolean {
    return this == Weather()
}


