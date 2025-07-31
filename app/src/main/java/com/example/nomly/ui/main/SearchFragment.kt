package com.example.nomly.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nomly.databinding.FragmentSearchBinding
import com.example.nomly.model.Recipe
import com.example.nomly.ui.home.RecipeAdapter

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Replace with real adapter implementation
        binding.searchResultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
/*        binding.searchResultsRecyclerView.adapter = RecipeAdapter(listOf()) { recipe ->
            // TODO: Navigate to recipe detail
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private lateinit var recipeAdapter: RecipeAdapter

    private fun setupRecyclerView(recipes: List<Recipe>) {
        recipeAdapter = RecipeAdapter(
            recipes,
            onItemClick = { recipe ->
                Toast.makeText(requireContext(), recipe.title, Toast.LENGTH_SHORT).show()
            },
            onFavoriteClick = { recipe ->
                // Handle favorite click here or leave empty for now
            }
        )


        binding.searchResultsRecyclerView.apply { // ✅ Correct ID here
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }
    }


}