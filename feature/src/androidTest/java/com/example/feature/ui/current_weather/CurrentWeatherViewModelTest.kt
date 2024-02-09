package com.example.feature.ui.current_weather

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import app.cash.turbine.test
import com.example.common.Language
import com.example.common.Units
import com.example.data.repo_impl.AppPreferencesRepository
import com.example.database.WeatherAppDatabase
import com.example.database.dao.WeatherDao
import com.example.feature.data.repo_impl.FakeWeatherRepositoryImpl
import com.example.model.Coord
import com.example.model.PreferenceModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
@ExperimentalCoroutinesApi
class CurrentWeatherViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_dataStore")
    lateinit var dataStore: DataStore<Preferences>

    @Inject
    @Named("test_room")
    lateinit var testDatabase: WeatherAppDatabase

    private lateinit var testPreferencesRepository: AppPreferencesRepository
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var fakeWeatherRepository: FakeWeatherRepositoryImpl
    private lateinit var testWeatherDao: WeatherDao

    @Before
    fun setUp() {
        hiltRule.inject()
        testWeatherDao = testDatabase.getWeatherDao()
        testPreferencesRepository = AppPreferencesRepository(dataStore)
        fakeWeatherRepository = FakeWeatherRepositoryImpl(testWeatherDao)
        viewModel = CurrentWeatherViewModel(fakeWeatherRepository, testPreferencesRepository)
    }

    @Test
    fun setDefaultPreferences_should_set_English_and_Metric() = runTest {
        withContext(Dispatchers.IO) {
            viewModel.setDefaultPreferences()
            testPreferencesRepository.getLanguage().test {
                assertThat(awaitItem()).isEqualTo("")
                advanceTimeBy(1000)
                assertThat(awaitItem()).isEqualTo(Language.ENG.lang)
            }
            testPreferencesRepository.getUnit().test {
                assertThat(awaitItem()).isEqualTo("")
                advanceTimeBy(1000)
                assertThat(awaitItem()).isEqualTo(Units.METRIC.value)
            }
        }
    }


    @Test
    fun setDefaultCity_should_set_City() = runTest {
        withContext(Dispatchers.IO) {
            val cityValues = Pair("test_location", Coord(-1.0, -1.0))
            viewModel.setDefaultCity(cityValues.first, cityValues.second)
            testPreferencesRepository.getDefaultCity().test {
                assertThat(awaitItem()).isEqualTo(Pair("",Coord(-1.0, -1.0)))
                advanceUntilIdle()
                assertThat(awaitItem()).isEqualTo(cityValues)

            }
        }
    }


    //should run above methods before calling this one so the dataStore values are set
    @Test
    fun preferences_should_return() = runTest {
        withContext(Dispatchers.IO) {
            viewModel.preferences.test {
                assertThat(awaitItem()).isNotNull()
                advanceTimeBy(1000)
                assertThat(awaitItem()).isEqualTo(PreferenceModel(location = "test_location", language = Language.ENG.lang, unit = Units.METRIC.value, profile = null))
            }
        }
    }

    @Test
    fun locationData_should_return() = runTest {
        viewModel.locationData.update { Pair(Coord(-1.0, -1.0), 0L) }
        assertThat(viewModel.locationData.first()).isEqualTo(Pair(Coord(-1.0, -1.0), 0L))
    }

    @Test
    fun homeState_should_return_current_weekly_hourly_weather() = runTest {
        withContext(Dispatchers.IO) {
            val currentWeather = fakeWeatherRepository.getCurrentWeather(Coord(),"","")
            val weeklyWeather = fakeWeatherRepository.getWeeklyWeather(Coord(),"","")
            val hourlyWeather = fakeWeatherRepository.getHourlyWeather(Coord(),"","")


            viewModel.setCurrentWeatherNetwork(currentWeather)
            viewModel.setWeeklyWeatherNetwork(weeklyWeather)
            viewModel.setHourlyWeatherNetwork(hourlyWeather)
            viewModel.homeState.test {
                assertThat(awaitItem()).isEqualTo(
                    HomeState(
                        currentWeatherUIState = CurrentWeatherUIState.Loading,
                        hourlyWeatherUiState = HourlyWeatherUiState.Loading,
                        weeklyWeatherUiState = WeeklyWeatherUiState.Loading
                    )
                )
                advanceTimeBy(1000)
                assertThat(awaitItem()).isEqualTo(
                    HomeState(
                        currentWeatherUIState = CurrentWeatherUIState.Success(fakeWeatherRepository.currentWeatherResponseTest),
                        hourlyWeatherUiState = HourlyWeatherUiState.Success(fakeWeatherRepository.hourlyWeatherResponseTest),
                        weeklyWeatherUiState = WeeklyWeatherUiState.Success(fakeWeatherRepository.weeklyWeatherResponseTest)
                    )
                )
            }
        }

    }


    @After
    fun tearDown() = runBlocking {
        testPreferencesRepository.clearDataStore()
        testDatabase.clearAllTables()
        testDatabase.close()
    }
}