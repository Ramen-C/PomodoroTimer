// ProgressScreen.kt
package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.controller.TimerController
import com.example.pomodorotimer.model.CycleCount
import com.example.pomodorotimer.model.TaskTimeStat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    taskController: TaskController,
) {
    // 使用 Flow 和 collectAsState 监听数据变化
    val cyclesPerDay by taskController.getCyclesPerDayFlow().collectAsState(initial = emptyList())
    val taskTimeStats by taskController.getTaskTimeStatsFlow().collectAsState(initial = emptyList())
    val cyclesTrend by taskController.getCyclesTrendFlow().collectAsState(initial = emptyList())
    // 假设 getWorkTimeDistributionFlow() 已实现
    // val workTimeDistribution by taskController.getWorkTimeDistributionFlow().collectAsState(initial = emptyMap())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("进度跟踪") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. 每日完成的番茄周期数柱状图
            Text(text = "每日完成的番茄周期数", style = MaterialTheme.typography.titleMedium)
            DailyCycleBarChart(cyclesPerDay = cyclesPerDay)

            // 2. 任务时间分布饼图
            Text(text = "任务时间分布", style = MaterialTheme.typography.titleMedium)
            TaskTimePieChart(taskTimeStats = taskTimeStats)

            // 3. 番茄周期完成趋势折线图
            Text(text = "番茄周期完成趋势", style = MaterialTheme.typography.titleMedium)
            CycleTrendLineChart(cyclesTrend = cyclesTrend)

            // 4. 工作时间分布饼图（如果实现）
            // Text(text = "工作时间分布", style = MaterialTheme.typography.titleMedium)
            // WorkTimePieChart(workTimeDistribution = workTimeDistribution)
        }
    }
}
