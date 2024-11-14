package com.example.pomodorotimer.model

import android.app.Application
import androidx.lifecycle.*
import com.example.pomodorotimer.data.AppDatabase
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = AppDatabase.getDatabase(application).taskDao()
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    fun insertTask(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskDao.deleteTask(task)
    }
}
