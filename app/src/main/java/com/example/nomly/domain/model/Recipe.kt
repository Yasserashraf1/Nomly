package com.example.nomly.domain.model

data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: String,
    val cookingTime: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val videoUrl:String?= null
)