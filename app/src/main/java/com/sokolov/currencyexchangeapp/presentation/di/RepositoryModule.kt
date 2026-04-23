package com.sokolov.currencyexchangeapp.presentation.di

import com.sokolov.currencyexchangeapp.data.local.db.ExchangeRateDao
import com.sokolov.currencyexchangeapp.data.remote.api.ExchangeRateApi
import com.sokolov.currencyexchangeapp.data.repository.ExchangeRateRepositoryImpl
import com.sokolov.currencyexchangeapp.domain.repository.ExchangeRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideExchangeRateRepository(
        api: ExchangeRateApi,
        dao: ExchangeRateDao
    ): ExchangeRateRepository = ExchangeRateRepositoryImpl(api, dao)
}