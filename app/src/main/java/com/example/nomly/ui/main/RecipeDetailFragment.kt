package com.example.nomly.ui.main

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.example.nomly.data.local.db.AppDatabase
import com.example.nomly.data.local.db.entities.FavoriteRecipe
import com.example.nomly.data.repository.MealRepository
import com.example.nomly.ui.presentation.viewmodel.FavoriteViewModel
import com.example.nomly.ui.presentation.viewmodel.RecipeDetailViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

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
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Recipe Details"
        }
        setHasOptionsMenu(true)

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
                        ingredients = recipe.ingredients,
                        videoUrl = recipe.videoUrl
                    )

                    favoriteViewModel.isFavorite(recipe.id).observe(viewLifecycleOwner) { isFav ->
                        updateFavoriteButtonIcon(isFav)

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

                    watchVideoButton.setOnClickListener {
                        if (!recipe.videoUrl.isNullOrEmpty()) {
                            val videoId = extractYoutubeVideoId(recipe.videoUrl!!)
                            if (videoId != null) {
                                miniPlayerContainer.visibility = View.VISIBLE
                                lifecycle.addObserver(miniYoutubePlayerView)
                                miniYoutubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                    override fun onReady(youTubePlayer: YouTubePlayer) {
                                        youTubePlayer.loadVideo(videoId, 0f)
                                    }
                                })
                            } else {
                                Toast.makeText(requireContext(), "Invalid video URL", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), "No video available", Toast.LENGTH_SHORT).show()
                        }
                    }

                    closeMiniPlayerButton.setOnClickListener {
                        miniPlayerContainer.visibility = View.GONE
                        miniYoutubePlayerView.release()
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.recipe_not_found), Toast.LENGTH_SHORT).show()
            }
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

    private fun extractYoutubeVideoId(url: String): String? {
        return try {
            when {
                url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
                url.contains("youtu.be/") -> url.substringAfter("youtu.be/")
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
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
