package com.example.nomly.ui.main

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.nomly.R
import com.example.nomly.databinding.FragmentRecipeDetailBinding
import com.example.nomly.model.AppDatabase
import com.example.nomly.model.FavoriteRecipe
import com.example.nomly.repository.MealRepository
import com.example.nomly.ui.viewmodel.FavoriteViewModel
import com.example.nomly.ui.viewmodel.RecipeDetailViewModel

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RecipeDetailFragmentArgs by navArgs()
    private val recipeViewModel: RecipeDetailViewModel by viewModels()
    private lateinit var favoriteViewModel: FavoriteViewModel

    private var isInstructionsExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupFavoriteViewModel()

        recipeViewModel.loadRecipe(args.recipeId)

        recipeViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                binding.apply {
                    recipeTitle.text = recipe.title
                    Glide.with(this@RecipeDetailFragment)
                        .load(recipe.imageUrl)
                        .into(recipeImage)

                    recipeDescription.text = recipe.instructions.take(200) + "..."
                    ingredientsList.text = recipe.ingredients.joinToString(separator = "\n")
                    instructionsText.text = recipe.instructions

                    instructionsText.maxLines = 5
                    instructionsText.ellipsize = TextUtils.TruncateAt.END
                    toggleInstructionsButton.text = getString(R.string.show_more)

                    toggleInstructionsButton.setOnClickListener {
                        isInstructionsExpanded = !isInstructionsExpanded
                        if (isInstructionsExpanded) {
                            instructionsText.maxLines = Integer.MAX_VALUE
                            instructionsText.ellipsize = null
                            toggleInstructionsButton.text = getString(R.string.show_less)
                        } else {
                            instructionsText.maxLines = 5
                            instructionsText.ellipsize = TextUtils.TruncateAt.END
                            toggleInstructionsButton.text = getString(R.string.show_more)
                        }
                    }

                    val favoriteRecipe = FavoriteRecipe(
                        id = recipe.id,
                        title = recipe.title,
                        imageUrl = recipe.imageUrl,
                        instructions = recipe.instructions,
                        ingredients = recipe.ingredients
                    )

                    favoriteViewModel.isFavorite(recipe.id).observe(viewLifecycleOwner) { isFav ->
                        updateFavoriteButtonIcon(isFav)

                        // Update button text dynamically based on favorite state
                        saveFavoriteButton.text = if (isFav) {
                            getString(R.string.remove_from_favorites)
                        } else {
                            getString(R.string.save_as_favorite)
                        }

                        saveFavoriteButton.setOnClickListener {
                            if (isFav) {
                                favoriteViewModel.removeFromFavorites(favoriteRecipe)
                                Toast.makeText(requireContext(), getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show()
                            } else {
                                favoriteViewModel.addToFavorites(favoriteRecipe)
                                Toast.makeText(requireContext(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.recipe_not_found), Toast.LENGTH_SHORT).show()
            }
        }

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupFavoriteViewModel() {
        val db = AppDatabase.getDatabase(requireContext())
        val repository = MealRepository(db.favoriteRecipeDao())

        favoriteViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavoriteViewModel(repository) as T
                }
            }
        )[FavoriteViewModel::class.java]
    }

    private fun updateFavoriteButtonIcon(isFavorite: Boolean) {
        binding.saveFavoriteButton.icon = ContextCompat.getDrawable(
            requireContext(),
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
