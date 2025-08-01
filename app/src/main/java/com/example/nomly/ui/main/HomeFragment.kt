package com.example.nomly.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nomly.R
import com.example.nomly.databinding.FragmentHomeBinding
import com.example.nomly.model.FavoriteRecipe
import com.example.nomly.model.Recipe
import com.example.nomly.repository.MealRepository
import com.example.nomly.ui.home.RecipeAdapter
import com.example.nomly.ui.viewmodel.FavoriteViewModel
import com.example.nomly.ui.viewmodel.FavoriteViewModelFactory
import com.example.nomly.ui.viewmodel.RecipeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter

    private val viewModel: RecipeViewModel by viewModels()

    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Enable options menu
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Initialize FavoriteViewModel with factory, passing repository
        val dao = requireContext().let { context ->
            com.example.nomly.model.AppDatabase.getDatabase(context).favoriteRecipeDao()
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
                val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(recipe.id)
                findNavController().navigate(action)
            },
            onFavoriteClick = { recipe ->
                if (recipe.isFavorite) {
                    favoriteViewModel.removeFromFavorites(recipe.toFavoriteRecipe())
                } else {
                    favoriteViewModel.addToFavorites(recipe.toFavoriteRecipe())
                }
            }
        )

        binding.recipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }
    }

    private fun setupObservers() {
        // Observe favorites to update recipe list's favorite status
        favoriteViewModel.allFavorites.observe(viewLifecycleOwner) { favorites ->
            val favoriteIds = favorites.map { it.id }.toSet()
            viewModel.recipes.value?.let { recipes ->
                val updated = recipes.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
                recipeAdapter.submitList(updated)
            }
        }

        // Observe recipes from API or source
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            // When recipes are updated, merge favorite state
            val favoriteIds = favoriteViewModel.allFavorites.value?.map { it.id }?.toSet() ?: emptySet()
            val updated = recipes.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
            recipeAdapter.submitList(updated)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            // Optional: show/hide progress bar
        }
    }

    private fun setupSearchListener() {
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Not used
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val originalList = viewModel.recipes.value ?: return

                if (query.isEmpty()) {
                    recipeAdapter.submitList(originalList)
                    return
                }

                val filteredList = originalList.filter { recipe ->
                    recipe.title.contains(query, ignoreCase = true) ||
                            recipe.ingredients.any { ingredient ->
                                ingredient.contains(query, ignoreCase = true)
                            } ||
                            recipe.instructions.contains(query, ignoreCase = true)
                }

                recipeAdapter.submitList(filteredList)
            }
        })
    }
    // Helper extension to convert Recipe to FavoriteRecipe
    private fun Recipe.toFavoriteRecipe(): FavoriteRecipe {
        return FavoriteRecipe(
            id = this.id,
            title = this.title,
            imageUrl = this.imageUrl,
            instructions = this.instructions,
            ingredients = this.ingredients
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu) // Inflate the menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.aboutCreator -> {
                // Navigate to AboutFragment
                findNavController().navigate(R.id.aboutFragment)
                true
            }
            R.id.signOut -> {
                // Handle sign out action
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

