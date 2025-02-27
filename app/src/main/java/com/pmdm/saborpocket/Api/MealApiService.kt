package com.pmdm.saborpocket.Api

import com.pmdm.saborpocket.Entities.MealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("search.php")
    fun searchMeals(@Query("s") mealName: String): Call<MealResponse>
}