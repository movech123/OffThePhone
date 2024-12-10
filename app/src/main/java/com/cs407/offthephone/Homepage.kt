package com.cs407.offthephone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class Homepage : AppCompatActivity() {
    private lateinit var date_box: TextView
    private lateinit var database: TaskDatabase // Reference to the database
    private lateinit var screenTimeManager: ScreenTimeManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                                                                                                                                    setContentView(R.layout.activity_homepage)
        database = TaskDatabase.getDatabase(this) // Initialize the database

        setDate() // make sure the right date is displayed
        setupSchedule() // populate the schedule box with the user's current schedule
        setupButtons() // initialize buttons
        setupScreenTimeManager() // make sure we can access the user's screentime
        setScreentimeGraph() // make sure the graph is showing the right data

    }

    private fun setDate() {
        // set date string in the layout
        date_box = findViewById(R.id.current_date)
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        val currentDate = sdf.format(Date())
        date_box.text = currentDate
    }

    private fun setupSchedule() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSchedule)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            // Fetch incomplete tasks from the database
            val tasks = database.taskDao().getIncompleteTasks()

            // Get the top 3 tasks
            val topTasks = tasks.take(3).map { task ->
                ScheduleItem(task.name, task.time)
            }

            // Update the RecyclerView adapter
            recyclerView.adapter = ScheduleAdapter(topTasks)
        }
    }


    private fun setupButtons() {
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
        // Find the schedule button and set a click listener
        val scheduleButton = findViewById<CardView>(R.id.scheduleBox)
        scheduleButton.setOnClickListener {
            runOnUiThread {
                startActivity(Intent(this, TaskMaker::class.java))
            }
        }
        // Find the screen time button and set a click listener
        val screentimeButton = findViewById<CardView>(R.id.screentimeBox)
        screentimeButton.setOnClickListener {
            runOnUiThread {
                startActivity(Intent(this, ScreenTime::class.java))
            }
        }
    }

    private fun setupScreenTimeManager() {
        // setup ScreenTimeManager
        screenTimeManager = ScreenTimeManager(this)
        // Check and request permission if not granted
        if (!screenTimeManager.isUsageStatsPermissionGranted()) {
            //Toast.makeText(this, "Permission required to fetch screen time", Toast.LENGTH_SHORT).show()
            screenTimeManager.requestUsageStatsPermission()
        }
    }

    private fun setScreentimeGraph() {
        screenTimeManager.sendTopUsedApps(5) { topApps ->
            if (topApps.isEmpty()) {
                runOnUiThread {
                    Toast.makeText(this, "No screen time data available", Toast.LENGTH_SHORT).show()
                }
                return@sendTopUsedApps
            }

            val screentimeViews = listOf( // get list of screen time views
                findViewById<View>(R.id.screentime1),
                findViewById<View>(R.id.screentime2),
                findViewById<View>(R.id.screentime3),
                findViewById<View>(R.id.screentime4),
                findViewById<View>(R.id.screentime5)
            )
            val screentimeTexts = listOf( // get list of screen time texts
                findViewById<TextView>(R.id.bartext1),
                findViewById<TextView>(R.id.bartext2),
                findViewById<TextView>(R.id.bartext3),
                findViewById<TextView>(R.id.bartext4),
                findViewById<TextView>(R.id.bartext5)
            )

            // update the views according to screen time
            var maxTime: Long = 1
            val MAXHEIGHT = 305.0F
            val MINHEIGHT = 5.0F
            topApps.take(screentimeViews.size).forEachIndexed { index, (appName, screenTime) ->
                Log.d("TopApps", "App: $appName, Time: $screenTime milli-seconds")
                //Toast.makeText(this, "App: $appName, Time: $screenTimeInSeconds seconds", Toast.LENGTH_LONG).show()


                // Calculate height based on screen time
                val newHeight: Int
                if (index == 0) { // set most used to max height
                    maxTime = screenTime
                    newHeight = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        MAXHEIGHT,
                        resources.displayMetrics
                    ).toInt()
                } else {
                    newHeight = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        ((screenTime.toFloat() / maxTime) * MAXHEIGHT), // Dynamic height scaling
                        resources.displayMetrics
                    ).toInt()
                }

                // display the amout of time spent graphically
                val layoutParams = screentimeViews[index].layoutParams
                layoutParams.height = newHeight
                screentimeViews[index].layoutParams = layoutParams
                // display the app's name
                screentimeTexts[index].text = appName
            }
        }
    }

}