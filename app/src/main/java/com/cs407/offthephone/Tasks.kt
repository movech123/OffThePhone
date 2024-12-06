package com.cs407.offthephone

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Tasks : AppCompatActivity() {
    private lateinit var database : TaskDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tasks)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        // Init the DB
        database = TaskDatabase.getDatabase(this@Tasks)
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
        addTask("Task 1")
        loadTasks()
    }


    /**
     * Loads tasks from the database or storage.
     */
     private fun loadTasks() {
        lifecycleScope.launch {
            val tasks = database.taskDao().getTasks()
            println(tasks)

        }
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
        lifecycleScope.launch {
            val newTask = Task(0, 9, 10, listOf("Monday", "Tuesday"), taskName, false)
            database.taskDao().insertTask(newTask)
        }
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