// TimerScreen.kt
package com.example.pomodorotimer.ui

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.controller.TimerController
import kotlin.math.sqrt

@SuppressLint("DefaultLocale")
@Composable
fun TimerScreen(
    timerController: TimerController,
    taskController: TaskController,
    context: Context
) {
    val timeLeft by timerController.timeLeft.collectAsState()
    val isRunning by timerController.isRunning.collectAsState()
    val cycleInfo by timerController.cycleInfo.collectAsState()
    val currentTask by timerController.currentTask.collectAsState()
    val isAutoMode by timerController.isAutoMode.collectAsState()
    val promptShow by timerController.promptShow.collectAsState()
    val initialTime by timerController.initialTime.collectAsState()

    var showTaskDialog by remember { mutableStateOf(false) }
    var showPromptDialog by remember { mutableStateOf(false) }
    var promptMessage by remember { mutableStateOf("") }
    var showShakeWarningDialog by remember { mutableStateOf(false) }

    // 摇晃检测功能
    val isShakeToPauseEnabled by timerController.isShakeToPauseEnabled.collectAsState()

    // 设置 SensorManager 和传感器
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    val magnitude = sqrt(x * x + y * y + z * z)
                    if (magnitude > 15f && isRunning) { // 检测到摇晃时暂停计时
                        showShakeWarningDialog = true
                        timerController.pauseTimer()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // 注册传感器监听器
    LaunchedEffect(isRunning, isShakeToPauseEnabled) {
        if (isRunning && isShakeToPauseEnabled) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        } else {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    // 按钮颜色动画
    val buttonColor by animateColorAsState(
        targetValue = if (isRunning) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
        label = ""
    )

    // 计算进度
    val progress = if (initialTime > 0) timeLeft / initialTime.toFloat() else 0f

    // 监听提示显示
    LaunchedEffect(promptShow) {
        if (promptShow) {
            showPromptDialog = true
            promptMessage = if (isAutoMode && timerController.isCurrentLongBreak()) {
                "You have completed a round of work!"
            } else {
                "Current stage completed!"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showTaskDialog = true },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .width(200.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !isRunning
        ) {
            Text(
                text = currentTask?.name?.let { "Current Task：$it" } ?: "Select Task",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

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

        Box(
            modifier = Modifier
                .size(300.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
            )

            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.matchParentSize(),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 10.dp,
            )

            Text(
                text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }

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
                },
                enabled = (currentTask != null),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }

            Button(
                onClick = { timerController.resetTimer() },
                enabled = (currentTask != null)
            ) {
                Text("Reset")
            }
        }

        if (showPromptDialog) {
            AlertDialog(
                onDismissRequest = {
                    showPromptDialog = false
                    timerController.resetPromptShow()
                },
                title = { Text(text = "Hint") },
                text = { Text(promptMessage) },
                confirmButton = {
                    TextButton(onClick = {
                        showPromptDialog = false
                        timerController.resetPromptShow()
                    }) {
                        Text("Confirm")
                    }
                }
            )
        }

        if (showShakeWarningDialog) {
            AlertDialog(
                onDismissRequest = { showShakeWarningDialog = false },
                title = { Text(text = "Warning") },
                text = { Text("Do not shake the device, or the timer will pause!") },
                confirmButton = {
                    TextButton(onClick = { showShakeWarningDialog = false }) {
                        Text("Confirm")
                    }
                }
            )
        }
    }
}
