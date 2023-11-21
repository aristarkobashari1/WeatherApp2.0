package com.example.feature.ui.weather_list

import androidx.lifecycle.viewModelScope
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.WeatherRepository
import com.example.database.entity.Weather
import com.example.feature.navigation.NavigationViewModel
import com.example.model.Coord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val preferencesRepository: PreferencesRepository
) : NavigationViewModel() {

    val inputCoords= MutableStateFlow(Coord())

    private val searchedCity = inputCoords.flatMapLatest {
        if(it.lat!=0.0 && it.lon!==0.0)
            weatherRepository.getSearchedCity(coord = it)
        else
            emptyFlow()
    }

    fun getSearchedCity() = this.searchedCity


    init {
        searchedCity.launchIn(viewModelScope)
    }

    val weatherListDB = weatherRepository.getWeatherListFromDB()

    fun setDefaultCity(coords: Coord) = viewModelScope.launch {
        coords.apply {
            weatherRepository.getCity(this).collectLatest { city->
                preferencesRepository.setCity(city, this)
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

