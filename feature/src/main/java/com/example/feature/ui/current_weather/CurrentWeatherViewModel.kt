package com.example.feature.ui.current_weather

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
    private val weatherRepository: WeatherRepository,
    private val preferencesRepository: PreferencesRepository
) : NavigationViewModel() {

    fun setDefaultPreferences() { //set Default Language,Unit
        viewModelScope.launch {
            preferencesRepository.setLanguage(Language.ENG.name)
            preferencesRepository.setUnits(Units.METRIC.name)
            preferencesRepository.setCity("GLOBE", Coord(0.0,0.0))
        }
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

    val locationData: MutableStateFlow<Coord> = MutableStateFlow(Coord(0.0,0.0))

    private val currentWeatherNetwork = locationData.flatMapLatest { coordinates->
        weatherRepository.getCurrentWeather(coordinates).asResult()
    }

    private val weeklyWeatherNetwork = locationData.flatMapLatest { coordinates->
        weatherRepository.getWeeklyWeather(coordinates).asResult()
    }

    private val hourlyWeatherNetwork = locationData.flatMapLatest{ coordinates->
        weatherRepository.getHourlyWeather(coordinates).asResult()
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