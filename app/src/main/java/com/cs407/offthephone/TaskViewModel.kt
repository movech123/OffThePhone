package com.cs407.offthephone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData

class TasksViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    val tasks = liveData {
        emit(taskDao.getTasks()) // Emits the list of tasks to observers
    }
}