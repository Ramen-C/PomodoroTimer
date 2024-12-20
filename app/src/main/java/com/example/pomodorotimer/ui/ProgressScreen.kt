// ProgressScreen.kt
package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.controller.TaskController

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
            TopAppBar(title = { Text("Progress Tracking") })
        }
    ) { padding ->
        // 使用 LazyColumn 替代 Column + verticalScroll
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp) // 组件间距
        ) {
            // 1. 每日完成的番茄周期数柱状图
            item {
                Text(text = "Number of Pomodoro Cycles Completed Daily", style = MaterialTheme.typography.titleMedium)
                DailyCycleBarChart(
                    cyclesPerDay = cyclesPerDay,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // 设置固定高度
                )
            }

            // 2. 任务时间分布饼图
            item {
                Text(text = "Task Time Distribution", style = MaterialTheme.typography.titleMedium)
                TaskTimePieChart(
                    taskTimeStats = taskTimeStats,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // 设置固定高度
                )
            }

            // 3. 番茄周期完成趋势折线图
            item {
                Text(text = "Pomodoro Cycle Completion Trend", style = MaterialTheme.typography.titleMedium)
                CycleTrendLineChart(
                    cyclesTrend = cyclesTrend,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // 设置固定高度
                )
            }
        }
    }
}
