// TimerScreen.kt
package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.controller.TimerController

@Composable
fun TimerScreen(
    timerController: TimerController,
    taskController: TaskController
) {
    val timeLeft by timerController.timeLeft.collectAsState()
    val isRunning by timerController.isRunning.collectAsState()
    val cycleInfo by timerController.cycleInfo.collectAsState()
    val currentTask by timerController.currentTask.collectAsState()
    val isAutoMode by timerController.isAutoMode.collectAsState()
    val promptShow by timerController.promptShow.collectAsState()

    var showTaskDialog by remember { mutableStateOf(false) }
    var showPromptDialog by remember { mutableStateOf(false) }
    var promptMessage by remember { mutableStateOf("") }

    // 监听 promptShow 状态
    LaunchedEffect(promptShow) {
        if (promptShow) {
            showPromptDialog = true
            // 设置提示消息
            promptMessage = if (isAutoMode && timerController.isCurrentLongBreak()) {
                "您已完成一轮工作！"
            } else {
                "当前阶段已完成！"
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

        // 显示倒计时
        Text(
            text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
            fontSize = 48.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // 显示当前周期信息
        Text(
            text = cycleInfo,
            fontSize = 20.sp
        )

        // 自动/手动模式切换按钮

        // 控制按钮：开始/暂停 和 重置
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

        // 提示对话框
        if (showPromptDialog) {
            AlertDialog(
                onDismissRequest = {
                    showPromptDialog = false
                    timerController.resetPromptShow()
                },
                title = { Text(text = "提示") },
                text = { Text(promptMessage) },
                confirmButton = {
                    TextButton(onClick = {
                        showPromptDialog = false
                        timerController.resetPromptShow()
                    }) {
                        Text("确定")
                    }
                }
            )
        }
    }
}
