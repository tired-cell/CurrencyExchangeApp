package com.sokolov.currencyexchangeapp.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromMap(value: Map<String, Double>): String = Gson().toJson(value)

    @TypeConverter
    fun toMap(value: String): Map<String, Double> {
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromLong(value: Long): java.util.Date = java.util.Date(value)

    @TypeConverter
    fun toLong(date: java.util.Date): Long = date.time
}