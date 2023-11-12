package com.example.feature.ui.current_weather

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.filters.MediumTest
import com.example.common.Language
import com.example.common.Units
import com.example.data.repo_impl.AppPreferencesRepository
import com.example.data.repository.WeatherRepository
import com.example.model.Coord
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
@MediumTest
class CurrentWeatherViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_dataStore")
    lateinit var dataStore: DataStore<Preferences>

    private lateinit var preferencesRepository:AppPreferencesRepository
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var weatherRepository: WeatherRepository


    @Before
    fun setUp(){
        hiltRule.inject()
        preferencesRepository = AppPreferencesRepository(dataStore)
        viewModel = CurrentWeatherViewModel(null,preferencesRepository)
    }

    @Test
    fun setDefaultPreferences_should_set_English_and_Metric() = runTest{
        viewModel.setDefaultPreferences()
        assertThat(preferencesRepository.getLanguage().first()).isEqualTo(Language.ENG.lang)
        assertThat(preferencesRepository.getUnit().first()).isEqualTo(Units.METRIC.value)
    }


    @Test
    fun setDefaultCity_should_set_City() = runTest {
        val cityValues = Pair("test", Coord(-1.0,-1.0))

        viewModel.setDefaultCity(cityValues.first,cityValues.second)
        assertThat(preferencesRepository.getDefaultCity().first()).isNotNull()
        assertThat(preferencesRepository.getDefaultCity().first()).isEqualTo(cityValues)
    }

    @Test
    fun preferences_should_return() = runTest {
        val preferences = viewModel.preferences.first()
//        assertThat(preferences).isEqualTo(
//            PreferenceModel(location = "test", language = Language.ENG.lang, unit = Units.METRIC.value,profile = null))
        assertThat(preferences).isNotNull()
    }

    @Test
    fun locationData_should_return() = runTest {
        val locationData = viewModel.locationData.first()
        assertThat(locationData).isNotNull()
    }

    @Test
    fun homeState_should_return_current_weekly_hourly_weather() = runTest {
//        val currentWeather = weatherRepository.getCurrentWeather()
//        val weeklyWeather = weatherRepository.getWeeklyWeather()
//        val hourlyWeather = weatherRepository.getHourlyWeather()
    }


}