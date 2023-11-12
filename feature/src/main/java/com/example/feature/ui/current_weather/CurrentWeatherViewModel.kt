package com.example.feature.ui.current_weather

import android.util.Log
import android.view.View
import androidx.lifecycle.viewModelScope
import com.example.common.Language
import com.example.common.Result
import com.example.common.Units
import com.example.common.asResult
import com.example.model.Coord
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.WeatherRepository
import com.example.database.entity.Weather
import com.example.feature.navigation.NavigationViewModel
import com.example.model.HourlyWeatherResponse
import com.example.model.PreferenceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository?,
    private val preferencesRepository: PreferencesRepository
) : NavigationViewModel() {

    fun setDefaultPreferences() { //set Default Language,Unit
        viewModelScope.launch {
            preferencesRepository.setLanguage(Language.ENG.lang)
            preferencesRepository.setUnits(Units.METRIC.value)
        }
    }

    fun setDefaultCity(city:String,coord: Coord) {
        viewModelScope.launch { preferencesRepository.setCity(city, coord)}
    }

    val preferences = combine(
        preferencesRepository.getLanguage(),
        preferencesRepository.getUnit(),
        preferencesRepository.getDefaultCity()
    ){ langRes,unitRes,cityRes ->
        PreferenceModel(cityRes.first,langRes,unitRes)
    }

    val dataStoreDefaultCity =preferencesRepository.getDefaultCity().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        Pair("",Coord())
    )

    val locationData: MutableStateFlow<Pair<Coord, Long>> = MutableStateFlow(Pair(Coord(0.0,0.0),0L))
        //need to hold a value to trigger changes if the coords dont exists
        //so the System.currentTimeMillis() to always trigger (especially for the swiperRefresh)

    var displayLoading:MutableStateFlow<Int> = MutableStateFlow(View.GONE)

    private val currentWeatherNetwork = locationData.flatMapLatest { coordinates->
        Log.e("Temp",coordinates.toString())
        weatherRepository!!.getCurrentWeather(coordinates.first).asResult()
    }

    private val weeklyWeatherNetwork = locationData.flatMapLatest { coordinates->
        weatherRepository!!.getWeeklyWeather(coordinates.first).asResult()
    }

    private val hourlyWeatherNetwork = locationData.flatMapLatest{ coordinates->
        weatherRepository!!.getHourlyWeather(coordinates.first).asResult()
    }

    val homeState = combine(
        currentWeatherNetwork,
        weeklyWeatherNetwork,
        hourlyWeatherNetwork,
    ) { currentWeatherResult, weeklyWeatherResult, hourlyWeatherResult ->

        val currentUi: CurrentWeatherUIState = when (currentWeatherResult) {
                is Result.Success -> CurrentWeatherUIState.Success(currentWeatherResult.data)
                is Result.Error -> CurrentWeatherUIState.Error(currentWeatherResult.throwable)
                Result.Loading -> CurrentWeatherUIState.Loading
            }

        val hourlyUi: HourlyWeatherUiState = when(hourlyWeatherResult){
                is Result.Success -> HourlyWeatherUiState.Success(hourlyWeatherResult.data)
                is Result.Error -> HourlyWeatherUiState.Error(hourlyWeatherResult.throwable)
                Result.Loading -> HourlyWeatherUiState.Loading
            }

        val weeklyUi: WeeklyWeatherUiState = when(weeklyWeatherResult){
            is Result.Success -> WeeklyWeatherUiState.Success(weeklyWeatherResult.data)
            is Result.Error -> WeeklyWeatherUiState.Error(weeklyWeatherResult.throwable)
            is Result.Loading -> WeeklyWeatherUiState.Loading
        }

        if(currentUi is CurrentWeatherUIState.Loading || hourlyUi is HourlyWeatherUiState.Loading || weeklyUi is WeeklyWeatherUiState.Loading)
            displayLoading.update {View.VISIBLE}
        else
            displayLoading.update {View.GONE}

        HomeState(currentUi,hourlyUi,weeklyUi)
    }
        .distinctUntilChanged { old, new ->
            old.currentWeatherUIState==new.currentWeatherUIState ||
            old.hourlyWeatherUiState==new.hourlyWeatherUiState ||
            old.weeklyWeatherUiState==new.weeklyWeatherUiState
        }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeState(
            currentWeatherUIState = CurrentWeatherUIState.Loading,
            hourlyWeatherUiState = HourlyWeatherUiState.Loading,
            weeklyWeatherUiState = WeeklyWeatherUiState.Loading
        )
    )

}

sealed interface CurrentWeatherUIState {
    data class Success(val data: Weather) : CurrentWeatherUIState
    data class Error(val throwable: Throwable? = null) : CurrentWeatherUIState
    object Loading : CurrentWeatherUIState
}

sealed interface HourlyWeatherUiState {
    data class Success(val data: HourlyWeatherResponse) : HourlyWeatherUiState
    data class Error(val throwable: Throwable? = null) : HourlyWeatherUiState
    object Loading : HourlyWeatherUiState
}

sealed interface WeeklyWeatherUiState{
    data class Success(val data: List<Weather>): WeeklyWeatherUiState
    data class Error(val throwable: Throwable? = null): WeeklyWeatherUiState
    object Loading: WeeklyWeatherUiState
}

data class HomeState(
    val currentWeatherUIState: CurrentWeatherUIState,
    val hourlyWeatherUiState: HourlyWeatherUiState,
    val weeklyWeatherUiState: WeeklyWeatherUiState
)