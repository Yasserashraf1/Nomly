package com.example.nomly.model
import com.example.nomly.model.FavoriteRecipe
data class MealDto(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String?,
    val strInstructions: String?,
    val strMealThumb: String?,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strYoutube: String?



)

fun MealDto.toRecipe(): Recipe {
    val ingredients = listOfNotNull(
        strIngredient1, strIngredient2, strIngredient3,
        strIngredient4, strIngredient5, strIngredient6
    ).filter { it.isNotBlank() }

    return Recipe(
        id = idMeal,
        title = strMeal,
        description = strCategory ?: "No description",
        ingredients = ingredients,
        instructions = strInstructions ?: "No instructions",
        cookingTime = "30 min", // placeholder
        imageUrl = strMealThumb ?: "",
        isFavorite = false,
        videoUrl = strYoutube
        )
}