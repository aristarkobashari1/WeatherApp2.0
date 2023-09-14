package com.example.data.repo_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.data.repository.PreferencesRepository
import com.example.model.Coord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AppPreferencesRepository @Inject constructor(
    private val userDataStorePreferences: DataStore<Preferences>
): PreferencesRepository {

    override suspend fun setLanguage(language: String) {
        userDataStorePreferences.edit { preference->
            preference[KEY_LANG] = language
        }
    }

    override suspend fun setUnits(units: String) {
        userDataStorePreferences.edit { preference->
            preference[KEY_UNIT] = units
        }
    }

    override suspend fun setCity(city: String, coord: Coord) {
        userDataStorePreferences.edit { preferences->
            preferences[KEY_DEFAULT_CITY_LAT] = coord.lat
            preferences[KEY_DEFAULT_CITY_LON] = coord.lon
            preferences[KEY_CITY] = city
        }
    }

    override fun getDefaultCity(): Flow<Pair<String, Coord>> = flow {
        userDataStorePreferences.data.collect { preference->
                val coordinates = Coord(lat = preference[KEY_DEFAULT_CITY_LAT]?:-1.0, lon = preference[KEY_DEFAULT_CITY_LON]?:-1.0)
                emit(Pair(preference[KEY_CITY]?:"",coordinates))
            }
    }

    override fun getLanguage(): Flow<String> = flow {
        userDataStorePreferences.data.collect{ preference->
            emit(preference[KEY_LANG]?:"")
        }
    }

    override fun getUnit(): Flow<String> = flow {
        userDataStorePreferences.data.collect{ preference ->
            emit(preference[KEY_UNIT]?:"")
        }
    }

    private companion object {
        val KEY_DEFAULT_CITY_LAT = doublePreferencesKey(name = "default_city_lat")
        val KEY_DEFAULT_CITY_LON = doublePreferencesKey(name = "default_city_lon")
        val KEY_LANG = stringPreferencesKey(name="language")
        val KEY_UNIT = stringPreferencesKey(name="unit")
        val KEY_CITY = stringPreferencesKey(name="city")
    }
}
