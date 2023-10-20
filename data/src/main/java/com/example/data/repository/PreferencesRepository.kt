package com.example.data.repository

import com.example.model.Coord
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun setLanguage(language: String)

    suspend fun setUnits(units:String)

    suspend fun setCity(city:String, coord: Coord)

    fun getDefaultCity(): Flow<Pair<String, Coord>>

    fun getLanguage(): Flow<String>

    fun getUnit(): Flow<String>

    suspend fun setLoggedUser(email:String, name:String)

    fun getLoggedUser(): Flow<Pair<String,String>>

    suspend fun clearLoggedUser()
}