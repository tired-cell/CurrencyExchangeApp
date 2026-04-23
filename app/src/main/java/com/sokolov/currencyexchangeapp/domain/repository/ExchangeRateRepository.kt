package com.sokolov.currencyexchangeapp.domain.repository

import com.sokolov.currencyexchangeapp.data.local.entity.QueryEntity
import com.sokolov.currencyexchangeapp.domain.model.ExchangeRate
import com.sokolov.currencyexchangeapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {
    fun getExchangeRates(baseCurrency: String): Flow<Resource<ExchangeRate>>
    fun getRateHistory(baseCurrency: String): Flow<List<ExchangeRate>>
    suspend fun saveQuery(queryParams: String): Long
    suspend fun updateQueryResult(queryId: Long, status: String, resultId: Long?)
    fun getAllQueries(): Flow<List<QueryEntity>>
}