package com.example.data.repo_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.data.repository.PreferencesRepository
import com.example.model.Coord
import com.example.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
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

    override suspend fun setLoggedUser(email: String, name: String, userImage:String) {
        userDataStorePreferences.edit { preferences->
            preferences[KEY_EMAIL] = email
            preferences[KEY_NAME] = name
            preferences[KEY_IMAGE] = userImage
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

    override fun getLoggedUser(): Flow<Profile> = channelFlow{
        userDataStorePreferences.data.collectLatest{preference->
            trySend(Profile(preference[KEY_NAME]?:"",preference[KEY_EMAIL]?:"",preference[KEY_IMAGE]?:"")).isSuccess
        }
    }

    override suspend fun clearLoggedUser() {
        userDataStorePreferences.edit { preferences->
            preferences.remove(KEY_EMAIL)
            preferences.remove(KEY_NAME)
            preferences.remove(KEY_IMAGE)
        }
    }

    override suspend fun clearDataStore() {
        userDataStorePreferences.edit {
            it.clear()
        }
    }

    private companion object {
        val KEY_DEFAULT_CITY_LAT = doublePreferencesKey(name = "default_city_lat")
        val KEY_DEFAULT_CITY_LON = doublePreferencesKey(name = "default_city_lon")
        val KEY_LANG = stringPreferencesKey(name="language")
        val KEY_UNIT = stringPreferencesKey(name="unit")
        val KEY_CITY = stringPreferencesKey(name="city")
        val KEY_EMAIL = stringPreferencesKey(name="email")
        val KEY_NAME = stringPreferencesKey(name="name")
        val KEY_IMAGE = stringPreferencesKey(name="image")
    }
}
