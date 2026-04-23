package com.sokolov.currencyexchangeapp.domain.usecase

import com.sokolov.currencyexchangeapp.domain.model.ExchangeRate
import com.sokolov.currencyexchangeapp.domain.model.Resource
import com.sokolov.currencyexchangeapp.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: ExchangeRateRepository
) {
    operator fun invoke(baseCurrency: String): Flow<Resource<ExchangeRate>> =
        repository.getExchangeRates(baseCurrency)
}