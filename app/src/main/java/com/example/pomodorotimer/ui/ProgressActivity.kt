// ProgressActivity.kt
package com.example.pomodorotimer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.controller.TimerController
import com.example.pomodorotimer.data.AppDatabase
import com.example.pomodorotimer.model.TaskModel
import com.example.pomodorotimer.theme.PomodoroTimerTheme

// ProgressActivity.kt
@ExperimentalMaterial3Api
class ProgressActivity : ComponentActivity() {
    private lateinit var taskController: TaskController
    private lateinit var timerController: TimerController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化 TaskModel 和 TaskController
        val taskDao = AppDatabase.getDatabase(applicationContext).taskDao()
        val cycleDao = AppDatabase.getDatabase(applicationContext).cycleDao()
        val taskModel = TaskModel(taskDao, cycleDao)
        taskController = TaskController(taskModel)
        timerController = TimerController(taskController)

        setContent {
            PomodoroTimerTheme {
                ProgressScreen(taskController,timerController)
            }
        }
    }
}
