// 文件：TaskActivity.kt
package com.example.pomodorotimer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.pomodorotimer.model.TaskViewModel
import com.example.pomodorotimer.theme.PomodoroTimerTheme

@ExperimentalMaterial3Api
class TaskActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTimerTheme {
                TaskScreen(taskViewModel, onTaskSelected = { task ->
                    // 处理任务选择，返回 MainActivity 或其他操作
                    finish() // 这里简单地关闭 Activity，实际应用中可传递数据
                })
            }
        }
    }
}
