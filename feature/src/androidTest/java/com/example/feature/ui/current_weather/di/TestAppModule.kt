package com.example.feature.ui.current_weather.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.di.userDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    @Named("test_dataStore") //so androidTest will inject this dataStore instead of the real app one
    fun provideUserDataStorePreferences(@ApplicationContext applicationContext: Context): DataStore<Preferences> {
        return applicationContext.userDataStore
    }
}