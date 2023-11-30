package com.example.feature.ui.weather_list

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class WeatherListFragmentTest{

    @Before
    fun setUp(){
        launchFragmentInContainer<WeatherListFragment>()
    }




}