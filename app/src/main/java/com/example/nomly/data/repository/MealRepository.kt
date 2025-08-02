package com.example.nomly.data.repository

import androidx.lifecycle.LiveData
import com.example.nomly.data.local.db.entities.FavoriteRecipe
import com.example.nomly.data.local.db.dao.FavoriteRecipeDao

class MealRepository(private val favoriteDao: FavoriteRecipeDao) {

    suspend fun addToFavorites(recipe: FavoriteRecipe) {
        favoriteDao.insert(recipe)
    }

    suspend fun removeFromFavorites(recipe: FavoriteRecipe) {
        favoriteDao.delete(recipe)
    }

    fun getAllFavorites() = favoriteDao.getAllFavorites()

    suspend fun isFavorite(id: String): Boolean {
        return favoriteDao.isFavorite(id)
    }

    suspend fun toggleFavorite(recipe: FavoriteRecipe) {
        if (isFavorite(recipe.id)) {
            removeFromFavorites(recipe)
        } else {
            addToFavorites(recipe)
        }
    }

    fun isFavoriteLive(id: String): LiveData<Boolean> {
        return favoriteDao.isFavoriteLive(id)
    }

}
