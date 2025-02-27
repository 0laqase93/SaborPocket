package com.pmdm.saborpocket.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pmdm.saborpocket.Data.Converters
import java.io.Serializable

data class MealResponse(
    val meals: List<RecetaEntity>?
)

@Entity(tableName = "RecetaEntity")
@TypeConverters(Converters::class)
data class RecetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    var label: String = "", // Nombre de la receta
    val image: String = "", // URL de la imagen de la receta
    val url: String = "", // URL a la receta original
    val source: String = "", // Fuente de la receta
    val yield: Int = 0, // Número de porciones
    val calories: Double = 0.0, // Calorías de la receta
    val totalWeight: Double = 0.0, // Peso total de los ingredientes
    val ingredients: List<String> = emptyList(), // Lista de ingredientes
    val instructions: String = "", // Instrucciones de preparación
    val totalNutrients: Map<String, Double> = emptyMap(), // Nutrientes totales
    val totalDaily: Map<String, Double> = emptyMap(), // Porcentaje de valor diario recomendado
    val dietLabels: List<String> = emptyList(), // Etiquetas dietéticas
    val healthLabels: List<String> = emptyList(), // Etiquetas de salud
    val cautions: List<String> = emptyList(), // Advertencias (alérgenos, etc.)
    val cuisineType: List<String> = emptyList(), // Tipo de cocina (ej. italiana, mexicana)
    val mealType: List<String> = emptyList(), // Tipo de comida (ej. desayuno, cena)
    val dishType: List<String> = emptyList(), // Tipo de plato (ej. ensalada, postre)
    val strTags: String? = null, // Etiquetas adicionales
    val strYoutube: String? = null, // URL de YouTube relacionada con la receta
    val totalTime: Int = 0, // Tiempo total en minutos
    var esFavorita: Boolean = false // Si la receta es favorita
): Serializable // El Serializable es para poder pasarlo entre activities

fun separarCantidadIngrediente(ingredient: String): Pair<String, String> {
    val regex = """(\d+\s*\w*\s*(\d+)?[\w]*)\s*(.*)""".toRegex()
    val matchResult = regex.find(ingredient)
    return if (matchResult != null) {
        val cantidad = matchResult.groupValues[1].trim()
        val nombre = matchResult.groupValues[3].trim()
        Pair(cantidad, nombre)
    } else {
        Pair("", ingredient)  // Si no hay coincidencias, devolvemos el ingrediente completo sin cantidad
    }
}
