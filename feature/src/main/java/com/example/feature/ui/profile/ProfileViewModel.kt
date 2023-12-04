package com.example.feature.ui.profile

import androidx.lifecycle.viewModelScope
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.WeatherRepository
import com.example.feature.navigation.NavigationViewModel
import com.example.model.PreferenceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val preferencesRepository: PreferencesRepository,
) : NavigationViewModel() {

    val initSignIn = MutableStateFlow(true)

    private val defCity = preferencesRepository.getDefaultCity()
    private val lang = preferencesRepository.getLanguage()
    private val unit = preferencesRepository.getUnit()
    val loggedUser = preferencesRepository.getLoggedUser()
    val isDarkModeEnabled = preferencesRepository.isDarkModeEnabled()


    val profileData = combine(
        defCity,
        lang,
        unit,
        loggedUser
    ) { cityRes, langRes, unitRes, loggedUser ->
        PreferenceModel(cityRes.first, langRes, unitRes, loggedUser)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        PreferenceModel()
    )


    fun setLoggedUser(email: String, name: String, userImage: String) {
        viewModelScope.launch {
            preferencesRepository.setLoggedUser(email, name, userImage)
        }
    }

    fun signOut() = viewModelScope.launch {
        preferencesRepository.clearLoggedUser()
        initSignIn.update { true }
    }

    fun setDefaultLanguage(language: String) =
        viewModelScope.launch { preferencesRepository.setLanguage(language) }

    fun setDefaultUnit(unit: String) =
        viewModelScope.launch { preferencesRepository.setUnits(unit) }

    fun changeDarkModeStatePref(switchState: Boolean) =
        viewModelScope.launch { preferencesRepository.setDarkMode(switchState) }

}
