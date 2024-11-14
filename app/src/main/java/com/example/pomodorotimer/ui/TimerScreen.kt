package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.model.TaskViewModel
import com.example.pomodorotimer.model.TimerViewModel
import java.util.Locale

@Composable
fun TimerScreen(
    timerViewModel: TimerViewModel,
    taskViewModel: TaskViewModel
) {
    val timeLeft = timerViewModel.timeLeft
    val isRunning = timerViewModel.isRunning
    val isWorking = timerViewModel.isWorking
    var workTime by remember { mutableStateOf(timerViewModel.workTime / 60) }

    var showDialog by remember { mutableStateOf(false) }
    var showTaskDialog by remember { mutableStateOf(false) }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    // 显示任务管理对话框
    if (showTaskDialog) {
        TaskDialog(
            taskViewModel = taskViewModel,
            onDismiss = { showTaskDialog = false },
            onTaskSelected = { task ->
                timerViewModel.currentTask = task
                showTaskDialog = false
            }
        )
    }

    // 显示设置工作时间对话框
    if (showDialog) {
        WorkTimeDialog(
            initialWorkTime = workTime,
            onDismiss = { showDialog = false },
            onConfirm = { newWorkTime ->
                timerViewModel.updateWorkTime(newWorkTime * 60)
                workTime = newWorkTime
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
            text = "当前任务：${timerViewModel.currentTask?.name ?: "未选择"}",
            fontSize = 20.sp
        )

        // 任务选择按钮
        Button(onClick = { showTaskDialog = true }, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("选择任务")
        }

        // 显示剩余时间
        Text(
            text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds),
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
                    timerViewModel.pauseTimer()
                } else {
                    timerViewModel.startTimer()
                }
            }) {
                Text(if (isRunning) "暂停" else "开始")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { timerViewModel.resetTimer() }) {
                Text("重置")
            }


        }
    }
}
