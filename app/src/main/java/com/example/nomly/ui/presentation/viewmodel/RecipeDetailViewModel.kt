package com.example.nomly.ui.presentation.viewmodel


import androidx.lifecycle.*
import com.example.nomly.domain.model.Recipe
import com.example.nomly.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeDetailViewModel : ViewModel() {

    private val repository = RecipeRepository()

    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: LiveData<Recipe?> get() = _recipe

    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private var currentRecipe: Recipe? = null

    fun loadRecipe(id: String) {
        viewModelScope.launch {
            currentRecipe = repository.getRecipeById(id)
            _recipe.value = currentRecipe

            // TODO: Load favorite state from local DB or preferences
            _isFavorite.value = false
        }
    }

    fun toggleFavorite() {
        val newFav = !(_isFavorite.value ?: false)
        _isFavorite.value = newFav

        // TODO: Save favorite state in DB or shared preferences
    }
}
