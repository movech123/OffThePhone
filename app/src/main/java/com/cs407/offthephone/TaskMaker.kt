package com.cs407.offthephone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class TaskMaker : AppCompatActivity() {
    private var database: TaskDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_taskmaker)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        database = TaskDatabase.getDatabase(this@TaskMaker)

        // Find the home button and set a click listener
        val homeButton: ImageView = findViewById(R.id.homeIcon)
        homeButton.setOnClickListener {
            runOnUiThread {
                val homeintent = Intent(this, Homepage::class.java)
                startActivity(homeintent)
            }
        }

        // Find the tasks button and set a click listener
        val checkButton: ImageView = findViewById(R.id.checkmarkIcon)
        checkButton.setOnClickListener {
            runOnUiThread {
                val taskintent = Intent(this, Tasks::class.java)
                startActivity(taskintent)
            }
        }

        // Find the settings button and set a click listener
        val settingButton: ImageView = findViewById(R.id.settingsIcon)
        settingButton.setOnClickListener {
            runOnUiThread {
                val settingsintent = Intent(this, Settings::class.java)
                startActivity(settingsintent)
            }
        }

        // Submit button logic
        val submitButton: Button = findViewById(R.id.submitButton)
        val taskField: EditText = findViewById(R.id.enterTask)
        val timeField: EditText = findViewById(R.id.enterTime)

        // Submit button logic
        submitButton.setOnClickListener {
            val newTaskName = taskField.text.toString()
            val newTaskTime = timeField.text.toString()

            if (newTaskName.isNotEmpty() && newTaskTime.isNotEmpty()) {
                lifecycleScope.launch {
                    // Add the task to the database
                    val newTask = Task(id = 0, name = newTaskName, time = newTaskTime) // `id = 0` allows Room to auto-generate it
                    database?.taskDao()?.insertTask(newTask)

                    // Clear the input fields
                    taskField.text.clear()
                    timeField.text.clear()

                    Toast.makeText(this@TaskMaker, "Task added successfully!", Toast.LENGTH_SHORT).show()

                    // Navigate to the Tasks page
                    val intent = Intent(this@TaskMaker, Tasks::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Please enter both task and time", Toast.LENGTH_SHORT).show()
            }
        }
    }
}