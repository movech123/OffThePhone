package com.cs407.offthephone

import android.os.Bundle
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
    }

    // TODO recognize when app is loaded and on homepage
    //  sleep for certain amt of time while app loads. Then:
    //  go to setup page if the user is new
    //  or homepage if they are not

    /**
     * Looks for user information on device.
     * @return true if there is already a user profile, false if not.
     */
    private fun findUser() {
        // TODO go to setup page if the user is new
        //  or homepage if they are not
    }
}