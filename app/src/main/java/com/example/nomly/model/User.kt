package com.example.nomly.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val favoriteRecipes: List<String> = emptyList()
)