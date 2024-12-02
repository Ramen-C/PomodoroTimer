package com.example.pomodorotimer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.controller.TimerController
import com.example.pomodorotimer.data.AppDatabase
import com.example.pomodorotimer.model.TaskModel

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取 Room 数据库实例
        val database = AppDatabase.getDatabase(applicationContext)

        // 获取 TaskDao
        val taskDao = database.taskDao()

        // 初始化 TaskModel 和 TaskController
        val taskModel = TaskModel(taskDao)
        val taskController = TaskController(taskModel)

        // 初始化 TimerController（确保有一个 TimerController）
        val timerController = TimerController()

        setContent {
            TimerScreen(
                timerController = timerController,  // 传递 TimerController
                taskController = taskController  // 传递 TaskController
            )
        }
    }
}