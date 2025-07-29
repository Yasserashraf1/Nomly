package com.example.nomly.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nomly.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveFavoriteButton.setOnClickListener {
            // TODO: Implement save as favorite logic
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}