package com.cs407.offthephone

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Schedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schedule)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        // Initialize the schedule view and load tasks for today.
        initializeSchedule()
    }

    /**
     * Initializes the schedule view and loads tasks for the current day.
     * Sets up listeners for task interactions (e.g., marking complete).
     */
    private fun initializeSchedule() {
        // TODO: Implement setup for schedule view.
        // - Load today's tasks and display them in a list or grid.
        // - Set up listeners for tasks, like marking tasks as complete or editing them.
    }

    /**
     * Adds a new task to the schedule.
     * @param taskDetails Details about the task (title, due time, priority, etc.).
     */
    private fun addTask(taskDetails: Tasks) {
        // TODO: Implement adding a task to the schedule.
        // - Save task details to the database or backend.
        // - Refresh the UI to display the new task.
    }

    /**
     * Edits an existing task in the schedule.
     * @param taskId The unique ID of the task to edit.
     * @param updatedDetails The updated details for the task.
     */
    private fun editTask(taskId: String, updatedDetails: Tasks) {
        // TODO: Implement task editing functionality.
        // - Update the task in the database.
        // - Reflect changes in the schedule view.
    }

    /**
     * Deletes a task from the schedule.
     * @param taskId The unique ID of the task to delete.
     */
    private fun deleteTask(taskId: String) {
        // TODO: Implement task deletion.
        // - Remove task from the database or backend.
        // - Update the UI to reflect the deleted task.
    }

    /**
     * Retrieves tasks for a selected date.
     * @param date The selected date for which to load tasks.
     */
    private fun loadTasksForDate(date: String) {
        // TODO: Implement loading of tasks for a specific date.
        // - Query database for tasks on 'date'.
        // - Update the schedule view to display these tasks.
    }

    /**
     * Marks a task as complete or incomplete.
     * @param taskId The unique ID of the task.
     * @param isComplete Boolean indicating completion status.
     */
    private fun markTaskComplete(taskId: String, isComplete: Boolean) {
        // TODO: Implement marking tasks as complete or incomplete.
        // - Update task status in the database.
        // - Refresh UI to indicate completion (e.g., strike-through, color change).
    }

}