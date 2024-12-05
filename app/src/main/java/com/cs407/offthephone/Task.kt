package com.cs407.offthephone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    val startTime : Int,
    val endTime : Int,
    val days : List<String>,
    val name : String,
    val isCompleted : Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int  = 0
)