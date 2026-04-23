package com.sokolov.currencyexchangeapp.presentation.di

import com.sokolov.currencyexchangeapp.domain.repository.ExchangeRateRepository
import com.sokolov.currencyexchangeapp.domain.usecase.GetExchangeRatesUseCase
dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGetExchangeRatesUseCase(repo: ExchangeRateRepository): GetExchangeRatesUseCase =
        GetExchangeRatesUseCase(repo)
}