// MainActivity.kt
package com.example.pomodorotimer.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.pomodorotimer.service.TimerService
import com.example.pomodorotimer.theme.PomodoroTimerTheme

class MainActivity : ComponentActivity() {
    private var timerService: TimerService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.LocalBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            timerService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 必须先调用super.onCreate
        super.onCreate(savedInstanceState)

        // 启动并绑定Service
        val serviceIntent = Intent(this, TimerService::class.java)
        startService(serviceIntent)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)

        // 获取数据库
        val database = AppDatabase.getDatabase(applicationContext)
        val taskDao = database.taskDao()
        val cycleDao = database.cycleDao()

        // 初始化 Model 与 Controller
        val taskModel = TaskModel(taskDao, cycleDao)
        val taskController = TaskController(taskModel)
        val timerController = TimerController(taskController)

        // 避免在onCreate中删除数据库，否则可能影响状态恢复。若确实需要，可注释掉此行。
        // AppDatabase.deleteDatabase(applicationContext)

        // 仅调用一次 setContent
        setContent {
            val currentTheme by timerController.currentTheme.collectAsState()
            PomodoroTimerTheme(theme = currentTheme) {
                PomodoroTimerApp(
                    taskController = taskController,
                    timerController = timerController
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
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
                    IconButton(onClick = { navController.navigate("settings") }) {
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
                    onClick = {
                        if (navController.currentBackStackEntry?.destination?.route != "timer") {
                            navController.navigate("timer") {
                                popUpTo("timer") { inclusive = true }
                            }
                        }
                    },
                    label = { Text("Timer") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Info, contentDescription = "Progress") },
                    selected = navController.currentBackStackEntry?.destination?.route == "progress",
                    onClick = {
                        if (navController.currentBackStackEntry?.destination?.route != "progress") {
                            navController.navigate("progress") {
                                popUpTo("progress") { inclusive = true }
                            }
                        }
                    },
                    label = { Text("Progress") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = "Settings") },
                    selected = navController.currentBackStackEntry?.destination?.route == "settings",
                    onClick = {
                        if (navController.currentBackStackEntry?.destination?.route != "settings") {
                            navController.navigate("settings") {
                                popUpTo("settings") { inclusive = true }
                            }
                        }
                    },
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
                val context = LocalContext.current
                TimerScreen(
                    timerController = timerController,
                    taskController = taskController,
                    context = context
                )
            }
            composable("progress") {
                ProgressScreen(
                    taskController = taskController,
                )
            }
            composable("settings") {
                SettingsScreen(
                    timerController = timerController
                )
            }
        }
    }
}
