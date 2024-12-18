// TaskActivity.kt
package com.example.pomodorotimer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.controller.TimerController
import com.example.pomodorotimer.data.AppDatabase
import com.example.pomodorotimer.model.TaskModel
import com.example.pomodorotimer.theme.PomodoroTimerTheme

@ExperimentalMaterial3Api
class TaskActivity : ComponentActivity() {
    private lateinit var taskController: TaskController
    private lateinit var timerController: TimerController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化 TaskModel 和 TaskController
        val taskDao = AppDatabase.getDatabase(applicationContext).taskDao()
        val cycleDao = AppDatabase.getDatabase(applicationContext).cycleDao()
        val taskModel = TaskModel(taskDao, cycleDao)
        taskController = TaskController(taskModel)

        // 初始化 TimerController（如需同步主题状态）
        timerController = TimerController(taskController)

        setContent {
            val currentTheme by timerController.currentTheme.collectAsState()
        }
    }
}
