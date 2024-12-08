package com.cs407.offthephone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Tasks : AppCompatActivity() {
    private lateinit var database : TaskDatabase
    private var taskList = arrayListOf<String>() // Store tasks locally
    private val viewModel: TasksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tasks)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        // Retrieve the task list from the intent
        val incomingTasks = intent.getStringArrayListExtra("TASK_LIST")
        if (incomingTasks != null) {
            taskList.addAll(incomingTasks)
        }

        // Display all tasks
        val taskContainer: LinearLayout = findViewById(R.id.toDoListContainer)
        viewModel.tasks.observe(this) { tasks ->
            // Clear previous views
            taskContainer.removeAllViews()

            // Update the taskList and the view
            taskList.clear()
            taskList.addAll(tasks.map { it.name })
            taskList.forEach { task ->
                addTaskToView(taskContainer, task)
            }
        }

        // Add button navigation to TaskMaker
        val addButton: Button = findViewById(R.id.addToDoButton)
        addButton.setOnClickListener {
            val intent = Intent(this, TaskMaker::class.java)
            intent.putStringArrayListExtra("TASK_LIST", taskList) // Pass the current list of tasks
            startActivity(intent)
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
        addTask("Task 2")


    }




    /**
     * Helper method to dynamically add tasks to the view
     */
    private fun addTaskToView(container: LinearLayout, task: String) {
        val taskLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val taskTextView = TextView(this).apply {
            text = task
            textSize = 18f
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val taskCheckbox = CheckBox(this).apply {
            setOnCheckedChangeListener { _, isChecked ->
                taskTextView.text = if (isChecked) "$task (Completed)" else task
            }
        }

        taskLayout.addView(taskTextView)
        taskLayout.addView(taskCheckbox)
        container.addView(taskLayout)
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