package com.example.database.di

import android.content.Context
import androidx.room.Room
import com.example.database.WeatherAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, WeatherAppDatabase::class.java, "weather_database")
            .addMigrations()
            .build()

    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherAppDatabase) = database.getWeatherDao()
}