package com.example.feature.ui.profile

import androidx.lifecycle.viewModelScope
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.WeatherRepository
import com.example.feature.navigation.NavigationViewModel
import com.example.model.PreferenceModel
import com.example.model.Profile
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

    private val defCity= preferencesRepository.getDefaultCity()
    private val lang = preferencesRepository.getLanguage()
    private val unit = preferencesRepository.getUnit()
    private val loggedUser = preferencesRepository.getLoggedUser()


    val profileData = combine(
        defCity,
        lang,
        unit,
        loggedUser
    ){ cityRes, langRes, unitRes,loggedUser ->
        PreferenceModel(cityRes.first,langRes,unitRes, Profile(loggedUser.first,loggedUser.second))
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        PreferenceModel()
    )


    fun setLoggedUser(email:String, name:String)= viewModelScope.launch {
        preferencesRepository.setLoggedUser(email, name)
    }

    fun signOut()= viewModelScope.launch {
        preferencesRepository.clearLoggedUser()
        initSignIn.update { true }
    }

}
