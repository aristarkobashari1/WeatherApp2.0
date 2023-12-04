package com.example.data.repository

import com.example.model.Coord
import com.example.model.Profile
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun setLanguage(language: String)

    suspend fun setUnits(units:String)

    suspend fun setCity(city:String, coord: Coord)

    fun getDefaultCity(): Flow<Pair<String, Coord>>

    fun getLanguage(): Flow<String>

    fun getUnit(): Flow<String>

    suspend fun setLoggedUser(email:String, name:String, userImage:String)

    fun getLoggedUser(): Flow<Profile>

    suspend fun clearLoggedUser()

    suspend fun clearDataStore()

    suspend fun setDarkMode(switch: Boolean)

    fun isDarkModeEnabled(): Flow<Boolean>
}