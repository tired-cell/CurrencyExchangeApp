package com.sokolov.currencyexchangeapp.presentation.di

import com.sokolov.currencyexchangeapp.data.remote.api.ExchangeRateApi
dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideExchangeRateApi(): ExchangeRateApi =
        Retrofit.Builder()
            .baseUrl("https://open.er-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
}