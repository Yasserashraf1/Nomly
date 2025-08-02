package com.example.nomly.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nomly.R
import com.example.nomly.ui.utils.SharedPrefs
import com.example.nomly.ui.main.RecipeActivity

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Delay for 4 seconds then navigate to login
        Handler(Looper.getMainLooper()).postDelayed({
            if (SharedPrefs.isLoggedIn(requireContext())){
                startActivity(Intent(requireContext(),RecipeActivity::class.java))
                requireActivity().finish()
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }

        },4000)
        }

}