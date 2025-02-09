package com.example.privaditaapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.privaditaapp.R
import com.example.privaditaapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

import android.util.Log
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // ui elements
    private lateinit var themeButton: ImageButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_users, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Handle navigation item selection
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    // Debug log for logout menu item
                    Log.d("MainActivity", "Logout menu item selected")
                    // Handle logout action
                    FirebaseAuth.getInstance().signOut()
                    // Redirect to login activity or perform other actions
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
                else -> {
                    // Let the NavController handle other menu items
                    val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
                    if (handled) {
                        drawerLayout.closeDrawer(navView)
                    }
                    handled
                }
            }
        }

        // Prevent the user from going back to the previous activity
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        // initialize ui elements
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        themeButton = findViewById(R.id.themeButton)
        userEmail = findViewById(R.id.userEmail)

        // get the email from the intent
        val email = intent.getStringExtra("email")
        userEmail.text = email

        sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)

        themeButton.setOnClickListener {
            val isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false)
            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putBoolean("isDarkTheme", false).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putBoolean("isDarkTheme", true).apply()
            }
        }
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}