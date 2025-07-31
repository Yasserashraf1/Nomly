package com.example.nomly.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nomly.R
import com.example.nomly.model.AppDatabase
import com.example.nomly.repository.MealRepository
import com.example.nomly.ui.viewmodel.FavoriteViewModel
import com.example.nomly.ui.viewmodel.FavoriteViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeActivity : AppCompatActivity() {

    lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        // Initialize database and repository
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = MealRepository(db.favoriteRecipeDao())
        val factory = FavoriteViewModelFactory(repository)

        // ✅ Correct: Use factory to create ViewModel
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        // Setup Bottom Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp()
    }
}
