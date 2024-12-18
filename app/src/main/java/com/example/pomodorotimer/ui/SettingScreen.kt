package com.example.pomodorotimer.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.controller.AppTheme
import com.example.pomodorotimer.controller.TimerController

@SuppressLint("DefaultLocale")
@Composable
fun SettingsScreen(timerController: TimerController) {
    // 原有状态和变量
    var workTime by remember { mutableIntStateOf(timerController.getCurrentWorkTimeInMinutes()) }
    var shortBreakTime by remember { mutableIntStateOf(timerController.getCurrentShortBreakTimeInMinutes()) }
    var longBreakTime by remember { mutableIntStateOf(timerController.getCurrentLongBreakTimeInMinutes()) }

    val isAutoMode by timerController.isAutoMode.collectAsState()
    var showConfirmation by remember { mutableStateOf(false) }
    var showModeSwitchMessage by remember { mutableStateOf<String?>(null) }

    // 获取当前主题和可用主题列表
    val currentTheme by timerController.currentTheme.collectAsState()
    val themes = listOf(AppTheme.RED, AppTheme.BLUE, AppTheme.GREEN)
    var expanded by remember { mutableStateOf(false) }

    // 摇晃检测
    val isShakeToPauseEnabled by timerController.isShakeToPauseEnabled.collectAsState()

    // 滚动状态
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(scrollState), // 启用滚动
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "设置",
            style = MaterialTheme.typography.headlineMedium
        )

        // 主题选择下拉框
        Box {
            Button(onClick = { expanded = true }) {
                Text("当前主题：${currentTheme.name}")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                themes.forEach { theme ->
                    DropdownMenuItem(
                        text = { Text(theme.name) },
                        onClick = {
                            timerController.setTheme(theme)
                            expanded = false
                        }
                    )
                }
            }
        }

        // **“摇晃停止计时”开关**
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "启用摇晃停止计时",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isShakeToPauseEnabled,
                onCheckedChange = { enabled ->
                    timerController.setShakeToPauseEnabled(enabled)
                }
            )
        }

        // **“启用自动模式”开关**
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "启用自动模式",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isAutoMode,
                onCheckedChange = { enabled ->
                    timerController.toggleAutoMode()
                }
            )
        }

        // **时间设置卡片**
        TimeSettingCard("工作时间（分钟）", workTime, 1f..60f, 59) { workTime = it }
        TimeSettingCard("短休息时间（分钟）", shortBreakTime, 1f..30f, 29) { shortBreakTime = it }
        TimeSettingCard("长休息时间（分钟）", longBreakTime, 1f..60f, 59) { longBreakTime = it }

        // **保存按钮**
        Button(
            onClick = {
                timerController.updateWorkTime(workTime)
                timerController.updateShortBreakTime(shortBreakTime)
                timerController.updateLongBreakTime(longBreakTime)
                showConfirmation = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp), // 增加底部边距，避免按钮过于贴近屏幕底部
            shape = MaterialTheme.shapes.medium
        ) {
            Text("保存设置")
        }

        // **模式切换信息弹窗**
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

        // **设置保存确认弹窗**
        if (showConfirmation) {
            AlertDialog(
                onDismissRequest = { showConfirmation = false },
                title = { Text("设置已保存") },
                text = { Text("新的设置已成功保存。是否立即刷新计时器？") },
                confirmButton = {
                    TextButton(onClick = {
                        showConfirmation = false
                        timerController.refreshTime()
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
            modifier = Modifier.padding(12.dp),
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
