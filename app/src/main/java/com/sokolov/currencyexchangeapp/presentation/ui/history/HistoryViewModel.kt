package com.sokolov.currencyexchangeapp.presentation.ui.history

import androidx.lifecycle.ViewModel
import com.sokolov.currencyexchangeapp.data.local.entity.QueryEntity
import com.sokolov.currencyexchangeapp.domain.repository.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    repository: ExchangeRateRepository
) : ViewModel() {
    val queries: Flow<List<QueryEntity>> = repository.getAllQueries()
}