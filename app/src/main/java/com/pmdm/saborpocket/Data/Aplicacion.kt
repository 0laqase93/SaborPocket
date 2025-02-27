package com.pmdm.saborpocket.Data

import android.app.Application
import androidx.room.Room
import com.pmdm.saborpocket.Entities.RecetaEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Aplicacion : Application() {
    companion object {
        lateinit var baseDeDatos: BaseDeDatos
    }

    override fun onCreate() {
        super.onCreate()

        baseDeDatos = Room.databaseBuilder(
            this,
            BaseDeDatos::class.java,
            "BaseDeDatos"
        )
            .fallbackToDestructiveMigration()
            .build()

        agregarRecetas()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun agregarRecetas() {
        GlobalScope.launch(Dispatchers.IO) {
            val recetasObtenidas = baseDeDatos
                .recetaDao()
                .obtenerTodasLasRecetas()

            if (recetasObtenidas.isEmpty()) {
                listOf(
                    RecetaEntity(
                        label = "Pasta Primavera",
                        image = "https://www.themealdb.com/images/media/meals/urxxxz1468242023.jpg",
                        url = "https://www.themealdb.com/meal/52772",
                        source = "TheMealDB",
                        yield = 4,
                        calories = 600.0,
                        totalWeight = 800.0,
                        ingredients = listOf("Pasta", "Tomates", "Pimientos", "Aceite de oliva", "Albahaca"),
                        instructions = "1. Cocina la pasta. 2. Sofríe los pimientos y tomates. 3. Mezcla todo.",
                        totalNutrients = mapOf("Carbs" to 120.0, "Proteins" to 25.0),
                        totalDaily = mapOf("Carbs" to 40.0, "Proteins" to 50.0),
                        dietLabels = listOf("Low-Carb"),
                        healthLabels = listOf("Vegetarian", "Gluten-Free"),
                        cautions = listOf("Contains Gluten"),
                        cuisineType = listOf("Italian"),
                        mealType = listOf("Lunch"),
                        dishType = listOf("Main"),
                        strTags = "Vegetarian,Healthy",
                        strYoutube = "https://www.youtube.com/watch?v=xyz123"
                    ),
                    RecetaEntity(
                        label = "Chicken Salad",
                        image = "https://www.themealdb.com/images/media/meals/abcxyz1468242023.jpg",
                        url = "https://www.themealdb.com/meal/52773",
                        source = "TheMealDB",
                        yield = 2,
                        calories = 350.0,
                        totalWeight = 400.0,
                        ingredients = listOf("Chicken", "Lettuce", "Tomatoes", "Cucumber", "Olive oil"),
                        instructions = "1. Grill the chicken. 2. Prepare the salad. 3. Toss together.",
                        totalNutrients = mapOf("Carbs" to 10.0, "Proteins" to 40.0),
                        totalDaily = mapOf("Carbs" to 15.0, "Proteins" to 80.0),
                        dietLabels = listOf("Low-Carb"),
                        healthLabels = listOf("High-Protein"),
                        cautions = listOf("Contains Nuts"),
                        cuisineType = listOf("American"),
                        mealType = listOf("Dinner"),
                        dishType = listOf("Salad"),
                        strTags = "Healthy,Protein-Rich",
                        strYoutube = "https://www.youtube.com/watch?v=abc456"
                    ),
                    RecetaEntity(
                        label = "Vegetable Stir Fry",
                        image = "https://www.themealdb.com/images/media/meals/xyz1231468242023.jpg",
                        url = "https://www.themealdb.com/meal/52850",
                        source = "TheMealDB",
                        yield = 3,
                        calories = 200.0,
                        totalWeight = 500.0,
                        ingredients = listOf("Broccoli", "Carrot", "Bell Pepper", "Soy Sauce", "Ginger"),
                        instructions = "1. Chop vegetables. 2. Stir fry with soy sauce and ginger.",
                        totalNutrients = mapOf("Carbs" to 30.0, "Proteins" to 5.0),
                        totalDaily = mapOf("Carbs" to 10.0, "Proteins" to 20.0),
                        dietLabels = listOf("Low-Calorie", "Vegan"),
                        healthLabels = listOf("Gluten-Free"),
                        cautions = listOf("May contain Soy"),
                        cuisineType = listOf("Asian"),
                        mealType = listOf("Lunch"),
                        dishType = listOf("Side"),
                        strTags = "Vegan,Healthy",
                        strYoutube = "https://www.youtube.com/watch?v=def789"
                    )
                )
                    .forEach { receta ->
                        baseDeDatos
                            .recetaDao()
                            .agregarReceta(receta)
                    }
            }
        }
    }
}