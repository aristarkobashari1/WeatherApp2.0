package com.example.feature.navigation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class NavigationViewModel: ViewModel() {
    private val _navigationCommand = MutableSharedFlow<NavigationCommand>()
    val navigationCommand = _navigationCommand

    suspend fun navigateTo(navDirections: NavDirections){
        _navigationCommand.emit(NavigationCommand.NavigateTo(navDirections))
    }

    suspend fun navigateBack(){
        _navigationCommand.emit(NavigationCommand.NavigateBack)
    }

}