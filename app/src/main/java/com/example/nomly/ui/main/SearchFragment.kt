package com.example.nomly.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nomly.data.local.db.AppDatabase
import com.example.nomly.databinding.FragmentSearchBinding
import com.example.nomly.data.local.db.entities.FavoriteRecipe
import com.example.nomly.domain.model.Recipe
import com.example.nomly.data.repository.MealRepository
import com.example.nomly.ui.home.RecipeAdapter
import com.example.nomly.ui.presentation.viewmodel.FavoriteViewModel
import com.example.nomly.ui.presentation.viewmodel.FavoriteViewModelFactory
import com.example.nomly.ui.presentation.viewmodel.RecipeViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter

    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = requireContext().let {
            AppDatabase.getDatabase(it).favoriteRecipeDao()
        }
        val repository = MealRepository(dao)
        val factory = FavoriteViewModelFactory(repository)
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupSearchListener()

        viewModel.fetchRecipes()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(
            emptyList(),
            onItemClick = { recipe ->
                val action = SearchFragmentDirections
                    .actionSearchFragmentToRecipeDetailFragment(recipe.id)
                findNavController().navigate(action)
                binding.search.setText("")
                recipeAdapter.submitList(emptyList())
            },
            onFavoriteClick = { recipe ->
                if (recipe.isFavorite) {
                    favoriteViewModel.removeFromFavorites(recipe.toFavoriteRecipe())
                } else {
                    favoriteViewModel.addToFavorites(recipe.toFavoriteRecipe())
                }
            }
        )

        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }
    }

    private fun setupObservers() {
        favoriteViewModel.allFavorites.observe(viewLifecycleOwner) { favorites ->
            val favoriteIds = favorites.map { it.id }.toSet()
            val originalList = viewModel.recipes.value ?: return@observe
            val updated = originalList.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
            recipeAdapter.submitList(updated)
        }

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            val favoriteIds =
                favoriteViewModel.allFavorites.value?.map { it.id }?.toSet() ?: emptySet()
            val updated = recipes.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
            recipeAdapter.submitList(updated)
        }
    }

    private fun setupSearchListener() {
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                val originalList = viewModel.recipes.value ?: return
                val filtered = filterRecipes(originalList, query)

                val favoriteIds =
                    favoriteViewModel.allFavorites.value?.map { it.id }?.toSet() ?: emptySet()
                val updated = filtered.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }

                recipeAdapter.submitList(updated)
            }
        })
    }

    fun filterRecipes(
        recipes: List<Recipe>,
        query: String
    ): List<Recipe> {
        val lowerQuery = query.trim().lowercase()

        return if (lowerQuery.isEmpty()) {
            recipes
        } else {
            recipes.filter { recipe ->
                recipe.title.contains(lowerQuery, ignoreCase = true) ||
                        recipe.instructions.contains(lowerQuery, ignoreCase = true) ||
                        recipe.ingredients.any { it.contains(lowerQuery, ignoreCase = true) }
            }
        }
    }

    private fun Recipe.toFavoriteRecipe(): FavoriteRecipe {
        return FavoriteRecipe(
            id = this.id,
            title = this.title,
            imageUrl = this.imageUrl,
            instructions = this.instructions,
            ingredients = this.ingredients,
            videoUrl = this.videoUrl
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        }
}