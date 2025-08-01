package com.example.nomly.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nomly.R
import com.example.nomly.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        // Enable the back button in the action bar
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Hide initially
        binding.aboutTitle.visibility = View.INVISIBLE
        binding.aboutDescription.visibility = View.INVISIBLE
        binding.aboutThanks.visibility = View.INVISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            binding.aboutTitle.visibility = View.VISIBLE
            binding.aboutDescription.visibility = View.VISIBLE
            binding.aboutThanks.visibility = View.VISIBLE
        }, 1000)
        binding.chef.speed = 0.7f
        // Handle back navigation using OnBackPressedDispatcher
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate back to the previous fragment
                requireActivity().supportFragmentManager.popBackStack()
            }

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu) // Inflate the menu if needed
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle back navigation when the home button is pressed
                requireActivity().supportFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
