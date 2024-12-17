package com.example.pomodorotimer.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign
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
    val isAutoMode by timerController.isAutoMode.collectAsState()
    val promptShow by timerController.promptShow.collectAsState()

    var showTaskDialog by remember { mutableStateOf(false) }
    var showPromptDialog by remember { mutableStateOf(false) }
    var promptMessage by remember { mutableStateOf("") }

    // 使用 remember 保存 totalTime，并在第一次渲染时设置它
    val totalTime = remember { timeLeft }  // 锁定 totalTime 为初始的 timeLeft

    // 计算进度条百分比
    val progress = timeLeft / totalTime.toFloat()

    // 监听 promptShow 状态
    LaunchedEffect(promptShow) {
        if (promptShow) {
            showPromptDialog = true
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
            .background(MaterialTheme.colorScheme.background) // 使用主题中的背景色
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 显示当前任务
        Text(
            text = "当前任务：${currentTask?.name ?: "未选择"}",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary, // 使用主色
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 任务选择按钮
        Button(
            onClick = { showTaskDialog = true },
            modifier = Modifier.padding(vertical = 8.dp),
            enabled = !isRunning // 计时中禁止更换任务
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

        // 显示倒计时进度条
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // 进度条的背景，使用浅红色填充
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
            )

            // 圆形进度条
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.matchParentSize(),
                color = MaterialTheme.colorScheme.primary, // 使用主色
                strokeWidth = 10.dp,
            )

            // 倒计时数字
            Text(
                text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.onPrimary, // 倒计时数字使用主要颜色的对比色
                textAlign = TextAlign.Center
            )
        }

        // 显示当前周期状态
        Text(
            text = cycleInfo,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary // 使用主色
        )

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
                },
                enabled = (currentTask != null) // 当前任务为空时按钮禁用
            ) {
                Text(if (isRunning) "暂停" else "开始")
            }

            Button(
                onClick = { timerController.resetTimer() },
                enabled = (currentTask != null) // 当前任务为空时按钮禁用
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
