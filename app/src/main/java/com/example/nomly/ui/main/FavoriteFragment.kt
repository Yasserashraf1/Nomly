package com.example.nomly.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private var favoriteList: List<Recipe> = emptyList()

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
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                if (query.isEmpty()) {
                    adapter.submitList(favoriteList)
                    return
                }
                val filteredList = favoriteList.filter {
                    it.title.contains(query, true) ||
                            it.ingredients.any { ingredient -> ingredient.contains(query, true) } ||
                            it.instructions.contains(query, true)
                }
                adapter.submitList(filteredList)
            }
        })

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
                    ingredients = recipe.ingredients,
                    videoUrl = recipe.videoUrl
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
                    ingredients = recipeToDelete.ingredients,
                    videoUrl = recipeToDelete.videoUrl
                )

                viewModel.removeFromFavorites(favoriteToDelete)
                Toast.makeText(requireContext(), "${recipeToDelete.title} removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.favoritesRecyclerView)

        viewModel.allFavorites.observe(viewLifecycleOwner) { favoriteEntities ->
            favoriteList = favoriteEntities.map { favorite ->
                Recipe(
                    id = favorite.id,
                    title = favorite.title,
                    description = favorite.instructions.take(100) + "...",
                    ingredients = favorite.ingredients ?: emptyList(),
                    instructions = favorite.instructions ?: "",
                    cookingTime = "30 mins",
                    imageUrl = favorite.imageUrl,
                    isFavorite = true,
                    videoUrl = favorite.videoUrl
                )
            }
            adapter.submitList(favoriteList)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        }
}