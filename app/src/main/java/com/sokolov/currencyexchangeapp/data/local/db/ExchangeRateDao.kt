package com.sokolov.currencyexchangeapp.data.local.db

import androidx.room.*
import com.sokolov.currencyexchangeapp.data.local.entity.ExchangeRateEntity
import com.sokolov.currencyexchangeapp.data.local.entity.QueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {
    @Query("SELECT * FROM exchange_rates WHERE baseCode = :baseCode ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestRateByBase(baseCode: String): ExchangeRateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: ExchangeRateEntity)

    @Query("DELETE FROM exchange_rates WHERE timestamp < :timestamp")
    suspend fun deleteOldRates(timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuery(query: QueryEntity): Long

    @Query("UPDATE search_history SET status = :status, resultId = :resultId WHERE id = :id")
    suspend fun updateQueryStatus(id: Long, status: String, resultId: Long?)

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    fun getAllQueries(): Flow<List<QueryEntity>>
}