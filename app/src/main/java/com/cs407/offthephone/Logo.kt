package com.cs407.offthephone

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Logo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (findUser()) {
                // Navigate to setup page if user is new
                startActivity(Intent(this, Setup::class.java))
            } else {
                // Navigate to homepage if user exists
                startActivity(Intent(this, Homepage::class.java))
            }
            finish() // Finish Logo activity so it’s not on the back stack
        }, 2000) // 2-second delay
    }


    // TODO recognize when app is loaded and on homepage
    //  sleep for certain amt of time while app loads. Then:
    //  go to setup page if the user is new
    //  or homepage if they are not
    // Delay and transition to next screen after a short time


/**
     * Looks for user information on device.
     * @return true if there is already a user profile, false if not.
     */
    private fun findUser(): Boolean {
       // val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return true
    }
}