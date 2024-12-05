package com.cs407.offthephone

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Tasks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tasks)
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

        // Find the settings button and set a click listener
        val settingButton: ImageView = findViewById(R.id.settingsIcon)
        settingButton.setOnClickListener {
            runOnUiThread {
                val settingsintent = Intent(this, Settings::class.java)
                startActivity(settingsintent)
            }
        }

        // Initialize task list and settings
        initializeTasks()
    }

    /**
     * Initializes task-related settings and loads tasks data.
     */
    private fun initializeTasks() {
        loadTasks()
        setupTaskCompletionListener()
    }

    /**
     * Loads tasks from the database or storage.
     */
    private fun loadTasks() {
        // TODO: Implement the logic to retrieve tasks from a database or local storage.
        // Example:
        // val tasks = TaskRepository.getTasksForToday()
    }

    /**
     * Sets up a listener or observer for task completion, allowing users to mark tasks as done.
     */
    private fun setupTaskCompletionListener() {
        // TODO: Implement listener for marking tasks as complete.
        // Example:
        // Set a checkbox listener for each task to update its completion status.
    }

    /**
     * Adds a new task to the user's list.
     * @param taskName The name of the task to add.
     */
    private fun addTask(taskName: String) {
        // TODO: Implement the logic to add a new task.
        // Example:
        // TaskRepository.addNewTask(Task(name = taskName))
    }

    /**
     * Deletes a task from the user's list.
     * @param taskId The unique identifier of the task to delete.
     */
    private fun deleteTask(taskId: Int) {
        // TODO: Implement task deletion logic.
        // Example:
        // TaskRepository.deleteTaskById(taskId)
    }

    /**
     * Suggests tasks for users based on their schedule, prioritizing tasks by deadline or importance.
     */
    private fun suggestTasks() {
        // TODO: Implement task suggestion logic based on user’s schedule.
        // Example:
        // val suggestedTasks = TaskSuggestionEngine.getSuggestedTasks()
    }

    /**
     * Marks a task as complete and updates the database.
     * @param taskId The unique identifier of the task to mark as complete.
     */
    private fun markTaskAsComplete(taskId: Int) {
        // TODO: Implement the logic to mark a task as complete.
        // Example:
        // TaskRepository.markAsComplete(taskId)
    }


}