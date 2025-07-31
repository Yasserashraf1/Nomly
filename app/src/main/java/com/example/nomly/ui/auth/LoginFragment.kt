package com.example.nomly.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nomly.databinding.FragmentLoginBinding
import com.example.nomly.ui.main.RecipeActivity
import androidx.core.content.edit

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to Register Screen
        binding.registerText.setOnClickListener {
            findNavController().navigate(
                com.example.nomly.R.id.action_loginFragment_to_registerFragment
            )
        }

        // Simulated login logic
        binding.loginButton.setOnClickListener {
            // Save fake login state
            val prefs = requireActivity().getSharedPreferences("auth", 0)
            prefs.edit() { putBoolean("logged_in", true) }

            // Go to RecipeActivity
            val intent = Intent(requireActivity(), RecipeActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Close login screen
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
