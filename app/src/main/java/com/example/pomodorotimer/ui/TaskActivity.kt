package com.example.pomodorotimer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.data.AppDatabase
import com.example.pomodorotimer.model.TaskModel
import com.example.pomodorotimer.theme.PomodoroTimerTheme

@ExperimentalMaterial3Api
class TaskActivity : ComponentActivity() {
    private lateinit var taskController: TaskController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 TaskModel 和 TaskController
        val taskDao = AppDatabase.getDatabase(applicationContext).taskDao()
        val taskModel = TaskModel(taskDao)
        taskController = TaskController(taskModel)

        setContent {
            PomodoroTimerTheme {
                // 将 TaskController 传递到 TaskScreen，并处理任务选择
                TaskScreen(taskController, onTaskSelected = { task ->
                    // 处理任务选择，返回 MainActivity 或其他操作
                    finish() // 这里简单地关闭 Activity，实际应用中可传递数据
                })
            }
        }
    }
}
