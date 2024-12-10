package com.cs407.offthephone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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
    private var taskMap = mutableMapOf<String, Task>() // Map task names to Task objects
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

        database = TaskDatabase.getDatabase(this@Tasks)
        // Retrieve the task list from the intent
        val incomingTasks = intent.getStringArrayListExtra("TASK_LIST")
        if (incomingTasks != null) {
            taskList.addAll(incomingTasks)
        }

        // Display all tasks
        val taskContainer: LinearLayout = findViewById(R.id.toDoListContainer)

        viewModel.tasks.observe(this) { tasks ->
            // Clear previous data
            taskList.clear()
            taskMap.clear()
            taskContainer.removeAllViews()

            // Populate taskList with names and taskMap with Task objects
            tasks.forEach { task ->
                taskList.add(task.name) // Add task name to the list
                taskMap[task.name] = task // Add Task object to the map
            }

            // Add tasks to the view
            taskList.forEach { taskName ->
                addTaskToView(taskContainer, taskName)
            }
        }

        // Add button navigation to TaskMaker
        val addButton: Button = findViewById(R.id.addToDoButton)
        addButton.setOnClickListener {
            val intent = Intent(this, TaskMaker::class.java)
            startActivity(intent)
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

    }

    /**
     * Helper method to dynamically add tasks to the view
     */
    private fun addTaskToView(container: LinearLayout, taskName: String) {
        val task = taskMap[taskName] ?: return // Get the Task object from the map

        val taskLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val taskTextView = TextView(this).apply {
            text = taskName
            textSize = 18f
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val deleteButton = Button(this).apply {
            text = getString(R.string.deleted_task) // Define "delete" text in strings.xml
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            visibility = Button.GONE // Initially hidden

            setOnClickListener {
                lifecycleScope.launch {
                    database.taskDao().deleteTask(task.id)
                    container.removeView(taskLayout)
                }
            }
        }

        val taskCheckbox = CheckBox(this).apply {
            isChecked = task.isCompleted // Use the `completed` field from the Task object

            setOnCheckedChangeListener { _, isChecked ->
                lifecycleScope.launch {
                    database.taskDao().updateTaskCompletion(task.id, isChecked) // Use the `id` field from the Task object
                    taskTextView.text = if (isChecked) "$taskName (Completed)" else taskName
                    deleteButton.visibility = if (isChecked) ImageButton.VISIBLE else ImageButton.GONE // Update trashButton visibility
                }
            }
        }

        taskLayout.addView(taskTextView)
        taskLayout.addView(taskCheckbox)
        taskLayout.addView(deleteButton)

        container.addView(taskLayout)
    }
}