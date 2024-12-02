package com.example.pomodorotimer.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.controller.TimerController
import com.example.pomodorotimer.model.Task
import androidx.compose.runtime.collectAsState

@SuppressLint("DefaultLocale")
@Composable
fun TimerScreen(
    timerController: TimerController,
    taskController: TaskController
) {
    // 使用 collectAsState() 来观察 TimerController 中的状态
    val timeLeft by timerController.timeLeft.collectAsState()
    val isRunning by timerController.isRunning.collectAsState()
    val isWorking by timerController.isWorking.collectAsState()

    var currentTask by remember { mutableStateOf<Task?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var showTaskDialog by remember { mutableStateOf(false) }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    // 显示任务管理对话框
    if (showTaskDialog) {
        TaskDialog(
            taskController = taskController,
            onDismiss = { showTaskDialog = false },
            onTaskSelected = { task ->
                currentTask = task
                timerController.currentTask = task  // 更新 TimerController 中的任务
                showTaskDialog = false
            }
        )
    }

    // 显示设置工作时间对话框
    if (showDialog) {
        WorkTimeDialog(
            initialWorkTime = minutes,
            onDismiss = { showDialog = false },
            onConfirm = { newWorkTime ->
                timerController.updateWorkTime(newWorkTime * 60)
                showDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 显示当前任务
        Text(
            text = "当前任务：${currentTask?.name ?: "未选择"}",
            fontSize = 20.sp
        )

        // 任务选择按钮
        Button(onClick = { showTaskDialog = true }, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("选择任务")
        }

        // 显示剩余时间
        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            fontSize = 48.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = if (isRunning) {
                if (isWorking) "工作中" else "休息中"
            } else {
                "暂停中"
            },
            fontSize = 20.sp
        )

        // 控制按钮
        Row(modifier = Modifier.padding(top = 16.dp)) {
            Button(onClick = { showDialog = true }) {
                Text("设置工作时间")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                if (isRunning) {
                    timerController.pauseTimer()
                } else {
                    timerController.startTimer()
                }
            }) {
                Text(if (isRunning) "暂停" else "开始")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { timerController.resetTimer() }) {
                Text("重置")
            }
        }
    }
}
