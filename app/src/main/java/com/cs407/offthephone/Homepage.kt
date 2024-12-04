package com.cs407.offthephone

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date

class Homepage : AppCompatActivity() {
    private lateinit var date_box: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // set date string in the layout
        date_box = findViewById(R.id.current_date)
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        val currentDate = sdf.format(Date())
        date_box.text = currentDate

        // Sample data for schedule items
        val scheduleList = listOf(
            ScheduleItem("Study", "12:00pm - 2:00pm"),
            ScheduleItem("Cook", "2:00pm - 5:00pm"),
            ScheduleItem("407 Meeting", "5:00pm - 8:00pm")
        )
        // Set up RecyclerView with adapter
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSchedule)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ScheduleAdapter(scheduleList)

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
                startActivity(Intent(this, Schedule::class.java))
            }
        }

        // Find the schedule button and set a click listener
        val screentimeButton = findViewById<CardView>(R.id.screentimeBox)
        screentimeButton.setOnClickListener {
            runOnUiThread {
                startActivity(Intent(this, ScreenTime::class.java))
            }
        }
    }

    private fun setupUI() {
        // Set up UI elements (buttons, text views, etc.)

    }

    private fun goToTasks() {
        // Intent to navigate to TasksActivity

    }

    private fun goToSettings() {
        // Intent to navigate to SettingsActivity
    }
}