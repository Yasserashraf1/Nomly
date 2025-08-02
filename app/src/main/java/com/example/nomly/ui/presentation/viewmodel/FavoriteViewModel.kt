package com.example.nomly.ui.presentation.viewmodel

import androidx.lifecycle.*
import com.example.nomly.data.local.db.entities.FavoriteRecipe
import com.example.nomly.data.repository.MealRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: MealRepository) : ViewModel() {

    fun addToFavorites(recipe: FavoriteRecipe) {
        viewModelScope.launch {
            repository.addToFavorites(recipe)
        }
    }

    fun removeFromFavorites(recipe: FavoriteRecipe) {
        viewModelScope.launch {
            repository.removeFromFavorites(recipe)
        }
    }

    fun isFavorite(recipeId: String): LiveData<Boolean> {
        return repository.isFavoriteLive(recipeId)
    }

    val allFavorites: LiveData<List<FavoriteRecipe>> = repository.getAllFavorites()
}
