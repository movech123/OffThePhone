package com.cs407.offthephone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ScreenTime : AppCompatActivity() {

    private lateinit var screenTimeManager: ScreenTimeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_time)

        setupButtons() // Initialize buttons

        screenTimeManager = ScreenTimeManager(this)

        if (!screenTimeManager.isUsageStatsPermissionGranted()) {
            screenTimeManager.requestUsageStatsPermission()
        } else {
            displayScreenTimeData()
        }
    }

    private fun setupButtons() {
        // Home button
        val homeButton: ImageView = findViewById(R.id.homeIcon)
        homeButton.setOnClickListener {
            val homeIntent = Intent(this, Homepage::class.java)
            startActivity(homeIntent)
        }

        // Checkmark button
        val checkButton: ImageView = findViewById(R.id.checkmarkIcon)
        checkButton.setOnClickListener {
            val taskIntent = Intent(this, Tasks::class.java)
            startActivity(taskIntent)
        }

        // Settings button
        val settingButton: ImageView = findViewById(R.id.settingsIcon)
        settingButton.setOnClickListener {
            val settingsIntent = Intent(this, Settings::class.java)
            startActivity(settingsIntent)
        }
    }

    private fun displayScreenTimeData() {
        screenTimeManager.sendTopUsedApps(5) { topApps ->
            if (topApps.isEmpty()) {
                runOnUiThread {
                    Toast.makeText(this, "No screen time data available", Toast.LENGTH_SHORT).show()
                }
                return@sendTopUsedApps
            }

            val sortedApps = topApps.sortedByDescending { it.second }

            runOnUiThread {
                // Update Bar Graph and App Names
                val barViews = listOf(
                    findViewById<View>(R.id.screentime1),
                    findViewById<View>(R.id.screentime2),
                    findViewById<View>(R.id.screentime3),
                    findViewById<View>(R.id.screentime4),
                    findViewById<View>(R.id.screentime5)
                )
                val appNameTexts = listOf(
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
                topApps.take(barViews.size).forEachIndexed { index, (appName, screenTime) ->
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
                    val layoutParams = barViews[index].layoutParams
                    layoutParams.height = newHeight
                    barViews[index].layoutParams = layoutParams
                    // display the app's name
                    appNameTexts[index].text = appName

                    // Update Seek Bars
                    val seekBars = listOf(
                        findViewById<SeekBar>(R.id.seekBar),
                        findViewById<SeekBar>(R.id.seekBar2),
                        findViewById<SeekBar>(R.id.seekBar3),
                        findViewById<SeekBar>(R.id.seekBar4),
                        findViewById<SeekBar>(R.id.seekBar5)
                    )
                    val appNames = listOf(
                        findViewById<TextView>(R.id.app_screentime1),
                        findViewById<TextView>(R.id.app_screentime2),
                        findViewById<TextView>(R.id.app_screentime3),
                        findViewById<TextView>(R.id.app_screentime4),
                        findViewById<TextView>(R.id.app_screentime5)
                    )
                    sortedApps.forEachIndexed { index, (appName, screenTime) ->
                        if (index < seekBars.size && index < appNames.size) {
                            val progress = ((screenTime.toFloat() / maxTime) * 100).toInt()
                            seekBars[index].progress = progress
                            // Convert time to hours and minutes
                            val hours = screenTime / (1000 * 60 * 60)
                            val minutes = (screenTime % (1000 * 60 * 60)) / (1000 * 60)
                            val timeString = when {
                                hours > 0 -> "${hours}h ${minutes}m"
                                else -> "${minutes}m"
                            }
                            appNames[index].text = "$appName: ${hours}h ${minutes}m"
                        }
                    }
                }
            }
        }
    }
}