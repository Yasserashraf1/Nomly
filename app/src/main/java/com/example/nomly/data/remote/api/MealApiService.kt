package com.example.nomly.data.remote.api

import com.example.nomly.data.remote.dto.MealDto
import retrofit2.http.GET
import retrofit2.http.Query

data class MealResponse(
    val meals: List<MealDto>?
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
