package com.example.nomly.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nomly.R
import com.example.nomly.data.local.db.AppDatabase
import com.example.nomly.data.repository.MealRepository
import com.example.nomly.ui.auth.AuthActivity
import com.example.nomly.ui.presentation.viewmodel.FavoriteViewModel
import com.example.nomly.ui.presentation.viewmodel.FavoriteViewModelFactory
import com.example.nomly.ui.utils.LanguageUtil
import com.example.nomly.ui.utils.SharedPrefs
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeActivity : AppCompatActivity() {

    lateinit var favoriteViewModel: FavoriteViewModel
    private var currentDestinationId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageUtil.loadLocale(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        // ViewModel setup
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = MealRepository(db.favoriteRecipeDao())
        val factory = FavoriteViewModelFactory(repository)
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        // Navigation setup
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.setupWithNavController(navController)

        // Handle title changes and menu refresh
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestinationId = destination.id
            supportActionBar?.title = when (destination.id) {
                R.id.homeFragment -> getString(R.string.home)
                R.id.searchFragment -> getString(R.string.search)
                R.id.favoriteFragment -> getString(R.string.favorites)
                R.id.recipeDetailFragment -> getString(R.string.recipe_details)
                R.id.aboutFragment -> getString(R.string.about)
                else -> getString(R.string.app_name)
            }
            invalidateOptionsMenu() // 🔁 Refresh menu on navigation
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp()
    }

    // Show or hide menu based on destination
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Don't show menu in AboutFragment
        return if (currentDestinationId == R.id.aboutFragment) {
            false
        } else {
            menuInflater.inflate(R.menu.options_menu, menu)
            true
        }
    }

    // Language switch handling
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_language_english -> {
                LanguageUtil.setLocale(this, "en")
                recreate()
                true
            }
            R.id.action_language_arabic -> {
                LanguageUtil.setLocale(this, "ar")
                recreate()
                true
            }
            R.id.aboutCreator -> {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.aboutFragment)
                true
            }

            R.id.signOut -> {
                SharedPrefs.logout(this)
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
