package com.cs407.offthephone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val startTime : Int,
    val endTime : Int,
    val days : List<String>,
    val name : String,
    val isCompleted : Boolean,


)