package com.example.feature.navigation

import androidx.navigation.NavDirections


sealed class NavigationCommand{
    data class NavigateTo(val direction: NavDirections): NavigationCommand()
    object NavigateBack: NavigationCommand()
}
