package com.example.data.di

import com.example.data.repo_impl.WeatherRepositoryImpl
import com.example.data.repository.WeatherRepository
import com.example.network.services.WeatherService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WeatherModule {

    @Binds
    fun provideWeatherModule(impl: WeatherRepositoryImpl): WeatherRepository
}