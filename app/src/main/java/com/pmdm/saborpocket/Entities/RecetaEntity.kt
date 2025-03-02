package com.pmdm.saborpocket.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "RecetaEntity")
data class RecetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    var label: String = "", // Nombre de la receta
    val image: String = "", // URL de la imagen de la receta
    val source: String = "", // Fuente de la receta
    val yield: Int = 0, // Número de porciones
    val calories: Double = 0.0, // Calorías de la receta
    val ingredients: Map<String, String> = emptyMap(), // Lista de ingredientes
    val instructions: List<String> = emptyList(), // Instrucciones de preparación
    val strYoutube: String? = null, // URL de YouTube relacionada con la receta
    val totalTime: Int = 0, // Tiempo total en minutos
    var esFavorita: Boolean = false // Si la receta es favorita
): Serializable // El Serializable es para poder pasarlo entre activities
