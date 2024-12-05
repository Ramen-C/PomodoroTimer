// ProgressScreen.kt
package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.model.TaskTimeStat

@Composable
fun ProgressScreen(taskController: TaskController) {
    val taskTimeStats = remember { mutableStateOf<List<TaskTimeStat>>(emptyList()) }

    LaunchedEffect(Unit) {
        taskController.getTaskTimeStats { stats ->
            taskTimeStats.value = stats
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "任务时间统计",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (taskTimeStats.value.isNotEmpty()) {
            TaskTimeStatsList(taskTimeStats.value)
        } else {
            Text(
                text = "暂无统计数据",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun TaskTimeStatsList(taskTimeStats: List<TaskTimeStat>) {
    Column {
        taskTimeStats.forEach { stat ->
            Text(
                text = "${stat.name}: ${stat.totalTimeSpent / 60} 分钟",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
