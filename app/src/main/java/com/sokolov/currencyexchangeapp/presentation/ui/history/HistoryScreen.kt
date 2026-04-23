package com.sokolov.currencyexchangeapp.presentation.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sokolov.currencyexchangeapp.data.local.entity.QueryEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val queries by viewModel.queries.collectAsStateWithLifecycle(emptyList())
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История запросов") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                }
            )
        }
    ) { padding ->
        if (queries.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { Text("История пуста") }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(queries.sortedByDescending { it.timestamp }, key = { it.id }) { q ->
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                        when (q.status) {
                            "SUCCESS" -> MaterialTheme.colorScheme.surfaceVariant
                            "FAILED" -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )) {
                        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                when (q.status) {
                                    "SUCCESS" -> Icons.Default.CheckCircle
                                    "FAILED" -> Icons.Default.Warning
                                    else -> Icons.Default.HourglassEmpty
                                },
                                null,
                                Modifier.size(40.dp),
                                tint = when (q.status) {
                                    "SUCCESS" -> Color(0xFF4CAF50)
                                    "FAILED" -> Color(0xFFF44336)
                                    else -> Color(0xFFFF9800)
                                }
                            )
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text("Валюта: ${q.queryParams}", style = MaterialTheme.typography.titleMedium)
                                Text(dateFormat.format(Date(q.timestamp)), style = MaterialTheme.typography.bodySmall)
                                Text("Статус: $ {
                                    when (q.status) {
                                        "SUCCESS" -> "✅ Успешно"
                                        "FAILED" -> "❌ Ошибка"
                                        else -> "⏳ В процессе"
                                    }
                                }", color = when (q.status) {
                                    "SUCCESS" -> Color(0xFF4CAF50)
                                    "FAILED" -> Color(0xFFF44336)
                                    else -> Color(0xFFFF9800)
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}