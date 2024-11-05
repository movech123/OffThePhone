package com.cs407.offthephone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Calendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calendar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize calendar view components and load user data.
        initializeCalendar()
    }

    /**
     * Initializes the calendar view, sets up date click listeners,
     * and loads events for the current month.
     */
    private fun initializeCalendar() {
        // TODO: Implement setup for calendar view.
        // - Configure the calendar view to display current month.
        // - Initialize listeners for date selection.
        // - Load events for the current month.
    }

    /**
     * Loads the events for the selected date from the database or storage.
     * @param date The date selected by the user.
     */
    private fun loadEventsForDate(date: String) {
        // TODO: Implement loading of events for the given date.
        // - Query local storage or backend for events scheduled on 'date'.
        // - Update the UI to display event details.
    }

    /**
     * Handles navigation between months in the calendar view.
     * @param direction Either "next" or "previous" month.
     */
    private fun navigateMonth(direction: String) {
        // TODO: Implement month navigation logic.
        // - Adjust the calendar view to show the next or previous month.
        // - Load events for the newly displayed month.
    }
}