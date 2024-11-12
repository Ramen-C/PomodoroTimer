package com.example.pomodorotimer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerScreen(timerViewModel: TimerViewModel) {
    val timeLeft = timerViewModel.timeLeft
    val isRunning = timerViewModel.isRunning

    // 将剩余时间转换为分钟和秒
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            fontSize = 64.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            Button(
                onClick = {
                    if (isRunning) {
                        timerViewModel.pauseTimer()
                    } else {
                        timerViewModel.startTimer()
                    }
                }
            ) {
                Text(if (isRunning) "暂停" else "开始")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { timerViewModel.resetTimer() }) {
                Text("重置")
            }
        }
    }
}
