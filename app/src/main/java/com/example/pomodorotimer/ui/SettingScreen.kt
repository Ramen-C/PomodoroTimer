// SettingScreen.kt
package com.example.pomodorotimer.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var showModeSwitchMessage by remember { mutableStateOf<String?>(null) }
    val isAutoMode by timerController.isAutoMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "设置",
            style = MaterialTheme.typography.headlineMedium
        )

        // 自动/手动切换按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    timerController.toggleAutoMode()
                    showModeSwitchMessage = if (isAutoMode) "当前模式：手动模式" else "当前模式：自动模式"
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isAutoMode) "切换为手动模式" else "切换为自动模式")
            }
        }

        // 工作时间设置
        TimeSettingCard("工作时间（分钟）", workTime, 1f..60f, 59) { workTime = it }

        // 短休息时间设置
        TimeSettingCard("短休息时间（分钟）", shortBreakTime, 1f..30f, 29) { shortBreakTime = it }

        // 长休息时间设置
        TimeSettingCard("长休息时间（分钟）", longBreakTime, 1f..60f, 59) { longBreakTime = it }

        // 统一的保存设置按钮
        Button(
            onClick = {
                timerController.updateWorkTime(workTime)
                timerController.updateShortBreakTime(shortBreakTime)
                timerController.updateLongBreakTime(longBreakTime)
                showConfirmation = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("保存设置")
        }

        // 模式切换弹窗
        showModeSwitchMessage?.let { message ->
            AlertDialog(
                onDismissRequest = { showModeSwitchMessage = null },
                title = { Text("模式") },
                text = { Text(message) },
                confirmButton = {
                    TextButton(onClick = { showModeSwitchMessage = null }) {
                        Text("确定")
                    }
                }
            )
        }

        // 确认弹窗
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

@Composable
fun TimeSettingCard(
    title: String,
    currentValue: Int,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = currentValue.toFloat(),
                onValueChange = { onValueChange(it.toInt()) },
                valueRange = valueRange,
                steps = steps
            )
            Text(
                text = "$currentValue 分钟",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
