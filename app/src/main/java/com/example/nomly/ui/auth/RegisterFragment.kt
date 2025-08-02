package com.example.nomly.ui.auth

import com.example.nomly.ui.main.RecipeActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nomly.R
import com.example.nomly.databinding.FragmentRegisterBinding
import com.example.nomly.model.AppDatabase
import com.example.nomly.model.User
import androidx.lifecycle.lifecycleScope
import com.example.nomly.model.UserEntity
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val userDao by lazy { AppDatabase.getDatabase(requireContext()).UserDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginText.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener {
            val username = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirm = binding.confirmPassword.text.toString()

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password != confirm) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val existingUser = userDao.getUser(username)
                if (existingUser != null) {
                    Toast.makeText(requireContext(), "User already exists", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    userDao.register(UserEntity(username, password))
                    Toast.makeText(
                        requireContext(),
                        "Registered successfully! Please login.",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        }
}