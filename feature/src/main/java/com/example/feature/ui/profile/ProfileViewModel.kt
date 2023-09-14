package com.example.feature.ui.profile

import androidx.lifecycle.viewModelScope
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.WeatherRepository
import com.example.feature.navigation.NavigationViewModel
import com.example.model.PreferenceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val preferencesRepository: PreferencesRepository
) : NavigationViewModel() {


    private val defCity= preferencesRepository.getDefaultCity()
    private val lang = preferencesRepository.getLanguage()
    private val unit = preferencesRepository.getUnit()


    val profileData = combine(
        defCity,
        lang,
        unit
    ){ cityRes, langRes, unitRes ->
        PreferenceModel(cityRes.first,langRes,unitRes)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        PreferenceModel()
    )


    fun logOut(){ // open dialog and clear preferences and database
    }

    fun logIn(){}
}
