package com.example.feature.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "test_preferences")

@Module
@InstallIn(SingletonComponent::class)
object TestDataStoreModule {

    @Provides
    @Singleton
    @Named("test_dataStore") //so androidTest will inject this dataStore instead of the real app one
    fun provideUserDataStorePreferences(@ApplicationContext applicationContext: Context): DataStore<Preferences> {
        return applicationContext.userDataStore
    }
}