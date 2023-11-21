package com.example.feature.ui.weather_list

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.example.data.repo_impl.AppPreferencesRepository
import com.example.database.WeatherAppDatabase
import com.example.database.dao.WeatherDao
import com.example.database.entity.Weather
import com.example.feature.data.repo_impl.FakeWeatherRepositoryImpl
import com.example.model.Coord
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class WeatherListViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_dataStore")
    lateinit var dataStore: DataStore<Preferences>

    @Inject
    @Named("test_room")
    lateinit var testDatabase: WeatherAppDatabase

    private lateinit var testPreferencesRepository: AppPreferencesRepository
    private lateinit var testWeatherRepository: FakeWeatherRepositoryImpl
    private lateinit var testViewModel: WeatherListViewModel
    private lateinit var testWeatherDao: WeatherDao

    @Before
    fun setUp(){
        hiltRule.inject()
        testWeatherDao = testDatabase.getWeatherDao()
        testPreferencesRepository = AppPreferencesRepository(dataStore)
        testWeatherRepository = FakeWeatherRepositoryImpl(testWeatherDao)
        testViewModel = WeatherListViewModel(testWeatherRepository,testPreferencesRepository)
    }


    @Test
    fun inputCoords_should_call_getSearchedCity() = runTest {
        withContext(Dispatchers.IO){
            val coords = Coord(-1.0,-1.0) //coords dont actually matters since the response is static, but needed to bypass the check in the searchedCity
            testViewModel.inputCoords.update { coords }
            testViewModel.getSearchedCity().test {
                assertThat(awaitItem()).isEqualTo(testWeatherRepository.currentWeatherResponseTest)
            }

            //also check if there exists a record of the obj in the database (if it didnt exists it should have been added)
            testWeatherDao.getWeather(testWeatherRepository.currentWeatherResponseTest.latitude!!,testWeatherRepository.currentWeatherResponseTest.longitude!!).test {
                assertThat(awaitItem()).isNotNull()
            }
        }
    }

    @Test
    fun setDefaultCity_should_getCity_fromDB_and_set_to_prefs() = runTest{
        withContext(Dispatchers.IO){
            testWeatherDao.insertEntity(Weather(location = "Test", latitude = "20.0", longitude = "20.0"))

            testViewModel.setDefaultCity(Coord(20.0,20.0))
            testPreferencesRepository.getDefaultCity().test {
                assertThat(awaitItem().first).isEqualTo("")
                advanceTimeBy(1000)
                assertThat(awaitItem().first).isEqualTo("Test")
            }
        }
    }

    @Test
    fun weatherListDB_should_return_weatherList() = runTest {
        withContext(Dispatchers.IO){
            //insert two empty weather obj to see if they will return
            testWeatherDao.insertEntity(Weather())
            testWeatherDao.insertEntity(Weather())
            testWeatherRepository.getWeatherListFromDB().test {
                assertThat(awaitItem().size).isEqualTo(2)
            }
        }
    }


    @After
    fun tearDown() = runBlocking{
        testPreferencesRepository.clearDataStore()
        testDatabase.clearAllTables()
        testDatabase.close()
    }
}