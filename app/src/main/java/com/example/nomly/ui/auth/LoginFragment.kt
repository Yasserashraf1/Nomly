package com.example.nomly.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nomly.databinding.FragmentLoginBinding
import com.example.nomly.ui.main.RecipeActivity
import androidx.lifecycle.lifecycleScope
import com.example.nomly.R
import com.example.nomly.ui.utils.SharedPrefs
import com.example.nomly.data.local.db.AppDatabase
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userDao by lazy {AppDatabase.getDatabase(requireContext()).UserDao() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {
            val username = binding.email.text.toString().trim()
            val password =  binding.password.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(requireContext(), "Please fill all fields",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val userFromRoom = userDao.getUser(username)
                val (savedUsername, savedPassword) = SharedPrefs.getUser(requireContext())

                if((userFromRoom != null && userFromRoom.password == password) ||
                    (savedUsername == username && savedPassword == password)){


                    SharedPrefs.setLogin(requireContext(), username, password)
                    startActivity(Intent(requireActivity(), RecipeActivity::class.java))
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
       }
}