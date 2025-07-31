package com.example.nomly.model


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: FavoriteRecipe)

    @Delete
    suspend fun delete(recipe: FavoriteRecipe)

    @Query("SELECT * FROM favorite_recipes")
    fun getAllFavorites(): LiveData<List<FavoriteRecipe>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id = :id)")
    fun isFavoriteLive(id: String): LiveData<Boolean>

}
