package com.sokolov.currencyexchangeapp.data.repository

import com.sokolov.currencyexchangeapp.data.local.db.ExchangeRateDao
import com.sokolov.currencyexchangeapp.data.local.entity.QueryEntity
import com.sokolov.currencyexchangeapp.data.remote.api.ExchangeRateApi
import com.sokolov.currencyexchangeapp.domain.model.ExchangeRate
import com.sokolov.currencyexchangeapp.domain.model.Resource
import com.sokolov.currencyexchangeapp.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi,
    private val dao: ExchangeRateDao
) : ExchangeRateRepository {

    override fun getExchangeRates(baseCurrency: String): Flow<Resource<ExchangeRate>> = flow {
        emit(Resource.Loading())
        val cached = dao.getLatestRateByBase(baseCurrency)
        if (cached != null) emit(Resource.Success(cached.toDomain()))
        try {
            val response = api.getLatestRates(baseCurrency)
            if (response.result == "success") {
                val entity = response.toEntity()
                dao.insertRate(entity)
                emit(Resource.Success(entity.toDomain()))
            } else if (cached == null) {
                emit(Resource.Error("API error"))
            }
        } catch (e: Exception) {
            if (cached == null) emit(Resource.Error(e.message ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getRateHistory(baseCurrency: String): Flow<List<ExchangeRate>> =
        flow { emit(emptyList()) }.flowOn(Dispatchers.IO)

    override suspend fun saveQuery(queryParams: String): Long =
        dao.insertQuery(QueryEntity(
            queryParams = queryParams,
            timestamp = System.currentTimeMillis(),
            status = "SUCCESS"
        ))

    override suspend fun updateQueryResult(queryId: Long, status: String, resultId: Long?) =
        dao.updateQueryStatus(queryId, status, resultId)

    override fun getAllQueries(): Flow<List<QueryEntity>> = dao.getAllQueries()
}