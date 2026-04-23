package com.sokolov.currencyexchangeapp.presentation.ui.main

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sokolov.currencyexchangeapp.domain.model.Resource
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    initialCurrency: String? = null,
    onNavigateToHistory: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var baseCurrency by remember { mutableStateOf(TextFieldValue(initialCurrency ?: "USD")) }
    val context = LocalContext.current
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    LaunchedEffect(initialCurrency) {
        initialCurrency?.takeIf { it.isNotBlank() }?.let {
            viewModel.loadExchangeRates(it)
            viewModel.saveQueryToHistory(it)
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Курсы валют", style = MaterialTheme.typography.headlineMedium)
            Row {
                IconButton(onClick = {
                    val text = (uiState as? MainViewModel.UiState.Success)?.exchangeRate?.rates
                        ?.entries?.take(5)?.joinToString { "
${it.key}=${"%.2f".format(it.value)}" } ?: ""
                    context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Курс ${baseCurrency.text}: $text")
                    }, "Поделиться"))
                }) { Icon(Icons.Default.Share, null) }
                IconButton(onClick = onNavigateToHistory) { Icon(Icons.Default.List, null) }
            }
        }

        OutlinedTextField(value = baseCurrency, onValueChange = { baseCurrency = it },
            label = { Text("Валюта (USD)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

        Button(onClick = {
            val c = baseCurrency.text.uppercase()
            viewModel.loadExchangeRates(c)
            viewModel.saveQueryToHistory(c)
        }, modifier = Modifier.fillMaxWidth()) { Text("Загрузить") }

        when (val s = uiState) {
            is MainViewModel.UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            is MainViewModel.UiState.Success -> s.exchangeRate?.let { r ->
                Text("База: ${r.baseCode}", style = MaterialTheme.typography.titleMedium)
                Text("Обновлено: ${dateFormat.format(r.timestamp)}")
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(r.rates.toList().sortedBy { it.first }) { (cur, rate) ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceBetween) {
                                Text(cur)
                                Text("%.4f".format(rate))
                            }
                        }
                    }
                }
            }
            is MainViewModel.UiState.Error -> Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)) {
                Text("Ошибка: ${s.message}", modifier = Modifier.padding(16.dp))
            }
        }
    }
}