package com.cs407.offthephone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Settings : AppCompatActivity() {
    class SettingsActivity : AppCompatActivity() {



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_settings)
            setupPreferences() // Initialize user preferences UI
        }

        private fun setupPreferences() {
            // Set up user preference UI elements
            // e.g., retrieve and display screen time limit settings
        }

        private fun savePreferences() {
            // Save user preferences
        }
    }
}