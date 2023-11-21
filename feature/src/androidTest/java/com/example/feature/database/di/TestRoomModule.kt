package com.example.feature.database.di

import android.content.Context
import androidx.room.Room
import com.example.database.WeatherAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestRoomModule {

    @Provides
    @Singleton
    @Named("test_room")
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, WeatherAppDatabase::class.java, "test_weather_database")
            .addMigrations()
            .build()
}