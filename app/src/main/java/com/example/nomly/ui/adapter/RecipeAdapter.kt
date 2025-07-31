package com.example.nomly.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nomly.R
import com.example.nomly.databinding.ItemRecipeBinding
import com.example.nomly.model.Recipe

class RecipeAdapter(
    internal var recipes: List<Recipe>,
    private val onItemClick: (Recipe) -> Unit,
    private val onFavoriteClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    fun submitList(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            // Set recipe title
            binding.recipeTitle.text = recipe.title

            // Set short description (truncate if longer than 100 chars)
            val shortDesc = if (recipe.instructions.length > 100)
                recipe.instructions.take(100) + "..."
            else
                recipe.instructions
            binding.recipeDescription.text = shortDesc

            // Set cooking time (you can customize)
            binding.cookingTime.text = recipe.cookingTime.ifEmpty { "30 mins" }

            // Load recipe image with Glide and placeholder
            Glide.with(binding.recipeImage.context)
                .load(recipe.imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.recipeImage)

            // Set favorite icon depending on isFavorite
            val iconRes = if (recipe.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            binding.favoriteIcon.setImageResource(iconRes)

            // Handle favorite icon click
            binding.favoriteIcon.setOnClickListener {
                onFavoriteClick(recipe)
            }

            // Handle item click
            binding.root.setOnClickListener {
                onItemClick(recipe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }
}
