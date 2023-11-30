package com.example.feature.epoxy

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.example.database.entity.Weather
import com.example.feature.CurrentWeatherTodayForecastBindingModel_

fun List<Weather>.modelToEpoxy(tempUnit: String): List<CurrentWeatherTodayForecastBindingModel_> {
    return map {
        CurrentWeatherTodayForecastBindingModel_()
            .id(it.id) //or UUID.randomUUID().toString()
            .weather(it)
            .tempUnit(tempUnit)
    }
}

fun <T> EpoxyRecyclerView.setUpGenericAdapter(
    list: List<T>, context: Context, modelBlock: (T, EpoxyController) -> Unit
) {

    this.layoutManager = LinearLayoutManager(context)
    this.withModels {
        list.forEach { model ->
            modelBlock(model, this)
        }
    }
}