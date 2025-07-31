package com.example.nomly.repository


import com.example.nomly.model.Recipe
import com.example.nomly.model.toRecipe
import com.example.nomly.repository.RetrofitInstance.api

class RecipeRepository {

    suspend fun getAllRecipes(): List<Recipe> {
        return try {
            val response = api.getAllMeals("") // explicitly pass empty string to get all meals
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

    // Fetch a specific recipe by ID
    suspend fun getRecipeById(id: String): Recipe? {
        return try {
            val response = api.getMealById(id)
            response.meals?.firstOrNull()?.toRecipe()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

