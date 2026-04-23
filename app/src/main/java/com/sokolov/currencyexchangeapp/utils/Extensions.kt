package com.sokolov.currencyexchangeapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toFormattedString(pattern: String = "dd.MM.yyyy HH:mm:ss"): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)