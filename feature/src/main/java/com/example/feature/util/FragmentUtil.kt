package com.example.feature.util

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.feature.navigation.NavigationCommand
import com.example.feature.navigation.NavigationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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


fun Context.setUpDialog(yesBlock:()->Unit) = MaterialAlertDialogBuilder(this)
    .setTitle("Attention")
    .setMessage("Do you want to set this location as default?")
    .setPositiveButton("Yes") { _, _ -> yesBlock }
    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
    .show()