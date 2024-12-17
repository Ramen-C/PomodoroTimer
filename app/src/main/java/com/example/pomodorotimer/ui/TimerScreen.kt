// TimerScreen.kt
package com.example.pomodorotimer.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
    val isAutoMode by timerController.isAutoMode.collectAsState()
    val promptShow by timerController.promptShow.collectAsState()

    var showTaskDialog by remember { mutableStateOf(false) }
    var showPromptDialog by remember { mutableStateOf(false) }
    var promptMessage by remember { mutableStateOf("") }

    // 为按钮点击添加动效
    val buttonColor by animateColorAsState(
        targetValue = if (isRunning) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
        label = ""
    )

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
        // 任务选择按钮
        Button(
            onClick = { showTaskDialog = true },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .width(200.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp), // 12dp 的圆角
            enabled = !isRunning
        ) {
            Text(
                text = currentTask?.name?.let { "当前任务：$it" } ?: "选择任务",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
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
                .size(300.dp)
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
                enabled = (currentTask != null), // 当前任务为空时按钮禁用
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor) // 动态改变按钮颜色
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


