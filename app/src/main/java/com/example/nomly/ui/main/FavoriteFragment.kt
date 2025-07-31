package com.example.nomly.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nomly.databinding.FragmentFavoriteBinding
import com.example.nomly.model.AppDatabase
import com.example.nomly.model.FavoriteRecipe
import com.example.nomly.model.Recipe
import com.example.nomly.repository.MealRepository
import com.example.nomly.ui.home.RecipeAdapter
import com.example.nomly.ui.viewmodel.FavoriteViewModel
import com.example.nomly.ui.viewmodel.FavoriteViewModelFactory

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RecipeAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.getDatabase(requireContext()).favoriteRecipeDao()
        val repository = MealRepository(dao)
        val factory = FavoriteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        adapter = RecipeAdapter(
            emptyList(),
            onItemClick = { recipe ->
                // Navigate to recipe detail
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToRecipeDetailFragment(recipe.id)
                findNavController().navigate(action)
            },
            onFavoriteClick = { recipe ->
                // Remove from favorites when heart icon clicked
                val favoriteRecipe = FavoriteRecipe(
                    id = recipe.id,
                    title = recipe.title,
                    imageUrl = recipe.imageUrl,
                    instructions = recipe.instructions,
                    ingredients = recipe.ingredients
                )
                viewModel.removeFromFavorites(favoriteRecipe)
            }
        )

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesRecyclerView.adapter = adapter

        // Attach swipe-to-delete functionality
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val recipeToDelete = adapter.recipes[position]

                val favoriteToDelete = FavoriteRecipe(
                    id = recipeToDelete.id,
                    title = recipeToDelete.title,
                    imageUrl = recipeToDelete.imageUrl,
                    instructions = recipeToDelete.instructions,
                    ingredients = recipeToDelete.ingredients
                )

                viewModel.removeFromFavorites(favoriteToDelete)
                Toast.makeText(requireContext(), "${recipeToDelete.title} removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.favoritesRecyclerView)

        viewModel.allFavorites.observe(viewLifecycleOwner) { favoriteList ->
            adapter.submitList(favoriteList.map { favorite ->
                Recipe(
                    id = favorite.id,
                    title = favorite.title,
                    description = favorite.instructions.take(100) + "...",
                    ingredients = favorite.ingredients ?: emptyList(),
                    instructions = favorite.instructions ?: "",
                    cookingTime = "30 mins",
                    imageUrl = favorite.imageUrl,
                    isFavorite = true
                )
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
