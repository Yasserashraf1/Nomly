package com.example.nomly.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipe(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val instructions: String,
    val ingredients: List<String>,
    val videoUrl:String?
)