package com.example.feature.ui.weather_list

import androidx.lifecycle.viewModelScope
import com.example.model.Coord
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.WeatherRepository
import com.example.database.entity.Weather
import com.example.feature.navigation.NavigationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val preferencesRepository: PreferencesRepository
) : NavigationViewModel() {

    val weatherListDB = weatherRepository.getWeatherListFromDB()

    private val city= MutableStateFlow("")

    fun onSearchTextChange(text: CharSequence?) {
        text?.let { query ->
            city.update {query.toString()}
        }
    }

    private val searchedCity = city.flatMapLatest {
        if(it.isNotEmpty())
            weatherRepository.getSearchedCity(it.lowercase(Locale.ROOT))
        else
            emptyFlow()
    }

    val cityResult = searchedCity.map{ it }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Weather()
    )

    fun setDefaultCity(coords: Coord) = viewModelScope.launch {
        coords.apply {
            weatherRepository.getCity(coords).collectLatest { city->
                preferencesRepository.setCity(city, coords)
            }
        }
        displaySelectedWeather(coords)
    }

    fun displaySelectedWeather(coords: Coord)= viewModelScope.launch {
        navigateTo(WeatherListFragmentDirections.navigateToCurrentWeather().apply {
            latitude = coords.lat.toString()
            longitude = coords.lon.toString()
        })
    }


}

