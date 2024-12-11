package com.cs407.offthephone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class NotificationPreference(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE)

    fun setNotificationEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    fun isNotificationEnabled(): Boolean {
        return sharedPreferences.getBoolean("notifications_enabled", true)
    }
}


class MySharedPrefs(context: Context) {
    companion object {
        const val PREFS_NAME = "MyPrefs"
        const val NOTIFICATION_ENABLED = "notification_enabled"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}
class Settings : AppCompatActivity() {
    private lateinit var notificationPreference: NotificationPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        notificationPreference = NotificationPreference(this)
        var mSharedPrefs = MySharedPrefs(this)

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
        val toggleButton  = findViewById<Button>(R.id.notificationsToggleButton)
        updateButtonState(toggleButton)
        toggleButton.setOnClickListener {
            val isEnabled = notificationPreference.isNotificationEnabled()
            notificationPreference.setNotificationEnabled(!isEnabled)

            if (isEnabled) {
                NotificationManagerCompat.from(this).cancelAll()
                mSharedPrefs.save(MySharedPrefs.NOTIFICATION_ENABLED, false)

            } else {
                mSharedPrefs.save(MySharedPrefs.NOTIFICATION_ENABLED, true)
            }

            updateButtonState(toggleButton)
        }
    }


private fun updateButtonState(button: Button) {
    val isEnabled = notificationPreference.isNotificationEnabled()
    button.text = if (isEnabled) "Turn Off Notifications" else "Turn On Notifications"
}



}