package com.example.feature.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.feature.navigation.NavigationCommand
import com.example.feature.navigation.NavigationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun Fragment.observeNavigation(navigationViewModel: NavigationViewModel){
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED){
            navigationViewModel.navigationCommand.collectLatest {navigationCommand ->
                when (navigationCommand){
                    is NavigationCommand.NavigateTo -> findNavController().navigate(navigationCommand.direction)
                    is NavigationCommand.NavigateBack -> findNavController().navigateUp()
                }
            }
        }
    }
}

fun Fragment.observeFlows(
    vararg flowBlocks: suspend () -> Unit,
    state: Lifecycle.State = Lifecycle.State.CREATED,
    ) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flowBlocks.forEach { flowBlock ->
                launch {
                    flowBlock()
                }
            }
        }
    }
}

fun changeDarkModeState(isDarkMode:Boolean){
    if (isDarkMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}
