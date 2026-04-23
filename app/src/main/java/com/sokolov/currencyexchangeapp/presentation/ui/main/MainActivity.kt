package com.sokolov.currencyexchangeapp.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sokolov.currencyexchangeapp.presentation.ui.history.HistoryScreen
import com.sokolov.currencyexchangeapp.presentation.ui.theme.CurrencyExchangeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var deepLinkCurrency: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deepLinkCurrency = extractCurrency(intent)
        setContent {
            CurrencyExchangeAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation(deepLinkCurrency)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        deepLinkCurrency = extractCurrency(intent)
        setContent {
            CurrencyExchangeAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation(deepLinkCurrency)
                }
            }
        }
    }

    private fun extractCurrency(intent: Intent): String? = when (intent.action) {
        Intent.ACTION_VIEW -> intent.data?.lastPathSegment?.uppercase()
        Intent.ACTION_SEND -> intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            "[A-Z]{3}".toRegex().find(it)?.value
        }
        else -> null
    }
}

@androidx.compose.runtime.Composable
fun AppNavigation(startCurrency: String?) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(initialCurrency = startCurrency, onNavigateToHistory = {
                navController.navigate("history")
            })
        }
        composable("history") {
            HistoryScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}