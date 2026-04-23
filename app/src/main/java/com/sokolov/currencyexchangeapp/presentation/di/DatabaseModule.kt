package com.sokolov.currencyexchangeapp.presentation.di

import android.content.Context
import com.sokolov.currencyexchangeapp.data.local.db.ExchangeRateDao
import com.sokolov.currencyexchangeapp.data.local.db.ExchangeRateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideExchangeRateDatabase(@ApplicationContext context: Context): ExchangeRateDatabase =
        ExchangeRateDatabase.getDatabase(context)

    @Provides
    fun provideExchangeRateDao(db: ExchangeRateDatabase): ExchangeRateDao = db.exchangeRateDao()
}