package com.example.pomodorotimer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.controller.TimerController
import com.example.pomodorotimer.data.AppDatabase
import com.example.pomodorotimer.model.TaskModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem

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
            PomodoroTimerApp(
                taskController = taskController,
                timerController = timerController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTimerApp(
    taskController: TaskController,
    timerController: TimerController
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pomodoro Timer") },
                actions = {
                    IconButton(onClick = { /* 打开设置页面 */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Timer") },
                    selected = navController.currentBackStackEntry?.destination?.route == "timer",
                    onClick = { navController.navigate("timer") },
                    label = { Text("Timer") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Info, contentDescription = "Progress") },
                    selected = navController.currentBackStackEntry?.destination?.route == "progress",
                    onClick = { navController.navigate("progress") },
                    label = { Text("Progress") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = "Settings") },
                    selected = navController.currentBackStackEntry?.destination?.route == "settings",
                    onClick = { navController.navigate("settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->
        // 使用 NavHost 来管理不同屏幕的切换
        NavHost(
            navController = navController,
            startDestination = "timer",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("timer") {
                TimerScreen(
                    timerController = timerController,
                    taskController = taskController
                )
            }
            composable("progress") {
                ProgressScreen() // 进度统计界面
            }
            composable("settings") {
                SettingsScreen() // 设置界面
            }
        }
    }
}
