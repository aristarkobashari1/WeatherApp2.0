package com.example.network.di

import android.content.Context
import com.example.common.Google
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GoogleModule {

    @Provides
    @Singleton
    fun provideOnTapClient(@ApplicationContext applicationContext: Context): SignInClient {
        return Identity.getSignInClient(applicationContext)
    }

    @Provides
    @Singleton
    fun provideSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(Google.WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
    }


}