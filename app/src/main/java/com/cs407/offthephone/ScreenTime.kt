package com.cs407.offthephone

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ScreenTime : AppCompatActivity() {

    private lateinit var SettingsButton : Button
    private lateinit var  HomeButton : Button
    private lateinit var  TasksButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_screen_time)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        SettingsButton = findViewById(R.id.settings_button)
        HomeButton = findViewById(R.id.home_button)
        TasksButton = findViewById(R.id.task_button)
    }


}