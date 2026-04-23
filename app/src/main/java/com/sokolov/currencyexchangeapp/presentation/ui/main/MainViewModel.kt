package com.sokolov.currencyexchangeapp.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokolov.currencyexchangeapp.domain.model.ExchangeRate
import com.sokolov.currencyexchangeapp.domain.model.Resource
import com.sokolov.currencyexchangeapp.domain.repository.ExchangeRateRepository
import com.sokolov.currencyexchangeapp.domain.usecase.GetExchangeRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    private val repository: ExchangeRateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadExchangeRates(baseCurrency: String) {
        getExchangeRatesUseCase(baseCurrency).onEach { res ->
            _uiState.value = when (res) {
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> res.data?.let { UiState.Success(it) } ?: UiState.Error("Нет данных")
                is Resource.Error -> UiState.Error(res.message ?: "Ошибка")
            }
        }.launchIn(viewModelScope)
    }

    fun saveQueryToHistory(currency: String) {
        viewModelScope.launch { repository.saveQuery(currency) }
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val exchangeRate: ExchangeRate?) : UiState()
        data class Error(val message: String) : UiState()
    }
}