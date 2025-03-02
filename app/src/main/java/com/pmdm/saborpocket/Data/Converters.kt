package com.pmdm.saborpocket.Data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // Conversión para Map<String, String>
    @TypeConverter
    fun fromMap(value: Map<String, String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMap(value: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, type)
    }

    // Conversión para List<String>
    @TypeConverter
    fun fromList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return try {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(value, type) ?: emptyList()
        } catch (e: Exception) {
            listOf(value) // Si no es un JSON válido, lo almacena como un solo elemento en la lista
        }
    }
}
