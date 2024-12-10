package com.example.pomodorotimer.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.controller.TimerController
@SuppressLint("DefaultLocale")
@Composable
fun SettingsScreen(timerController: TimerController) {
    var workTime by remember {
        mutableIntStateOf(timerController.getCurrentWorkTimeInMinutes())
    }
    var shortBreakTime by remember {
        mutableIntStateOf(timerController.getCurrentShortBreakTimeInMinutes())
    }
    var longBreakTime by remember {
        mutableIntStateOf(timerController.getCurrentLongBreakTimeInMinutes())
    }
    var showConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "设置",
            style = MaterialTheme.typography.headlineMedium
        )
        // 工作时间设置
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "工作时间（分钟）",
                    style = MaterialTheme.typography.titleMedium
                )
                Slider(
                    value = workTime.toFloat(),
                    onValueChange = { workTime = it.toInt() },
                    valueRange = 1f..60f, // 1分钟到60分钟
                    steps = 59
                )
                Text(
                    text = "$workTime 分钟",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = {
                        timerController.updateWorkTime(workTime)
                        showConfirmation = true
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("保存工作时间")
                }
            }
        }
        // 短休息时间设置
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "短休息时间（分钟）",
                    style = MaterialTheme.typography.titleMedium
                )
                Slider(
                    value = shortBreakTime.toFloat(),
                    onValueChange = { shortBreakTime = it.toInt() },
                    valueRange = 1f..30f, // 1分钟到30分钟
                    steps = 29
                )
                Text(
                    text = "$shortBreakTime 分钟",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = {
                        timerController.updateShortBreakTime(shortBreakTime)
                        showConfirmation = true
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("保存短休息时间")
                }
            }
        }
        // 长休息时间设置
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "长休息时间（分钟）",
                    style = MaterialTheme.typography.titleMedium
                )
                Slider(
                    value = longBreakTime.toFloat(),
                    onValueChange = { longBreakTime = it.toInt() },
                    valueRange = 1f..60f, // 1分钟到60分钟
                    steps = 59
                )
                Text(
                    text = "$longBreakTime 分钟",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = {
                        timerController.updateLongBreakTime(longBreakTime)
                        showConfirmation = true
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("保存长休息时间")
                }
            }
        }
        // 在确认弹窗的确定按钮中，调用refreshTime
        if (showConfirmation) {
            AlertDialog(
                onDismissRequest = { showConfirmation = false },
                title = { Text("设置已保存") },
                text = { Text("新的设置已成功保存。是否立即刷新计时器？") },
                confirmButton = {
                    TextButton(onClick = {
                        showConfirmation = false
                        timerController.refreshTime() // 刷新计时器时间
                    }) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmation = false }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}
