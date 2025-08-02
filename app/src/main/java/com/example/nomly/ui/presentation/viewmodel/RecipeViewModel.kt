package com.example.nomly.ui.presentation.viewmodel

import androidx.lifecycle.*
import com.example.nomly.domain.model.Recipe
import com.example.nomly.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    private val repository = RecipeRepository()

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> get() = _recipes

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Fetch all recipes (no category param)
    fun fetchRecipes() {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = repository.getAllRecipes()
                _recipes.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load recipes."
            } finally {
                _loading.value = false
            }
        }
    }
}
