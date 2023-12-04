package com.example.feature.util

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.setUpDialog(yesBlock: () -> Unit) = MaterialAlertDialogBuilder(this)
    .setTitle("Attention")
    .setMessage("Do you want to set this location as default?")
    .setPositiveButton("Yes") { _, _ -> yesBlock() }
    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
    .show()!!


fun Context.setUpRadioBtnDialog(
    title: String,
    items: Array<String>,
    selectedItem: String,
    block: (selectedItem: String) -> Unit
) = MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setSingleChoiceItems(items, items.indexOf(selectedItem)) { _, index ->
            block(items[index])
        }
        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        .show()
