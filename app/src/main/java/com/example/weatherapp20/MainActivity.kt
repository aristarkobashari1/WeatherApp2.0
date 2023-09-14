package com.example.weatherapp20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.feature.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchActivity<HomeActivity> { finish()  }
    }
}