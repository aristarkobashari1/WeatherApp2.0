package com.example.feature.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.feature.navigation.NavigationCommand
import com.example.feature.navigation.NavigationViewModel
import kotlinx.coroutines.flow.collectLatest

fun Fragment.observeNavigation(navigationViewModel: NavigationViewModel){
    this.viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        navigationViewModel.navigationCommand.collectLatest {navigationCommand ->
            when (navigationCommand){
                is NavigationCommand.NavigateTo -> findNavController().navigate(navigationCommand.direction)
                is NavigationCommand.NavigateBack -> findNavController().navigateUp()
            }
        }
    }
}