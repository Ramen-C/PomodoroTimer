package com.example.pomodorotimer.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.controller.TimerController

@SuppressLint("DefaultLocale")
@Composable
fun TimerScreen(
    timerController: TimerController,
    taskController: TaskController
) {
    val timeLeft by timerController.timeLeft.collectAsState()
    val isRunning by timerController.isRunning.collectAsState()
    val cycleInfo by timerController.cycleInfo.collectAsState()
    val currentTask by timerController.currentTask.collectAsState()
    var showTaskDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentTask) {
        if (currentTask == null) {
            taskController.getAllTasks { tasks ->
                // 如果有默认任务（如"学习"），则自动设置
                tasks.firstOrNull { it.name == "学习" }?.let {
                    timerController.setCurrentTask(it)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 显示当前任务
        Text(
            text = "当前任务：${currentTask?.name ?: "未选择"}",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 任务选择按钮
        Button(
            onClick = { showTaskDialog = true },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("选择任务")
        }

        // 任务选择对话框
        if (showTaskDialog) {
            TaskDialog(
                taskController = taskController,
                onDismiss = { showTaskDialog = false },
                onTaskSelected = { task ->
                    timerController.setCurrentTask(task)
                    showTaskDialog = false
                }
            )
        }

        Text(
            text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
            fontSize = 48.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = cycleInfo,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (isRunning) {
                        timerController.pauseTimer()
                    } else {
                        timerController.startTimer()
                    }
                }
            ) {
                Text(if (isRunning) "暂停" else "开始")
            }

            Button(
                onClick = { timerController.resetTimer() }
            ) {
                Text("重置")
            }
        }
    }
}

