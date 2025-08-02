package com.example.nomly.data.repository


import com.example.nomly.domain.model.Recipe
import com.example.nomly.data.remote.dto.toRecipe
import com.example.nomly.data.remote.api.RetrofitInstance.api

class RecipeRepository {

    suspend fun getAllRecipes(): List<Recipe> {
        return try {
            val response = api.getAllMeals("")
            response.meals?.map { it.toRecipe() } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    // Get recipe details by ID
    suspend fun getRecipeById(id: String): Recipe? {
        return try {
            val response = api.getMealById(id)
            response.meals?.firstOrNull()?.toRecipe()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    }

    suspend fun getRecipeById(id: String): Recipe? {
        return try {
            val response = api.getMealById(id)
            response.meals?.firstOrNull()?.toRecipe()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

