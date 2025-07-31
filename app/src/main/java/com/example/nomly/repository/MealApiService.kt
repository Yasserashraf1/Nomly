package com.example.nomly.repository

import com.example.nomly.model.MealDto
import retrofit2.http.GET
import retrofit2.http.Query

// Response wrapper with nullable meals list
data class MealResponse(
    val meals: List<MealDto>?  // Nullable to handle API null responses
)

interface MealApiService {

    @GET("search.php")
    suspend fun getAllMeals(
        @Query("s") searchQuery: String = ""
    ): MealResponse

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): MealResponse
}
