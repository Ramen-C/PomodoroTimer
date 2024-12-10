// TaskTimePieChart.kt
package com.example.pomodorotimer.ui

import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.pomodorotimer.model.TaskTimeStat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun TaskTimePieChart(taskTimeStats: List<TaskTimeStat>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                setUsePercentValues(true)
                setDrawHoleEnabled(true)
                holeRadius = 50f
                setTransparentCircleRadius(55f)
                legend.isEnabled = true
                legend.form = Legend.LegendForm.CIRCLE
                legend.textSize = 12f
            }
        },
        modifier = modifier
    ) { pieChart ->
        if (taskTimeStats.isEmpty()) {
            pieChart.clear()
            pieChart.invalidate()
            return@AndroidView
        }
        val entries = taskTimeStats.map { stat ->
            PieEntry(stat.totalTimeSpent.toFloat() / 60f, stat.name) // 将秒转换为分钟
        }
        val dataSet = PieDataSet(entries, "任务时间分布").apply {
            colors = listOf(
                AndroidColor.RED,
                AndroidColor.GREEN,
                AndroidColor.BLUE,
                AndroidColor.YELLOW,
                AndroidColor.CYAN,
                AndroidColor.MAGENTA,
                AndroidColor.LTGRAY,
                AndroidColor.DKGRAY
            )
            valueTextColor = AndroidColor.BLACK
            valueTextSize = 12f
        }
        val data = PieData(dataSet).apply {
            setDrawValues(true)
            setValueFormatter(com.github.mikephil.charting.formatter.PercentFormatter())
        }
        pieChart.data = data
        pieChart.invalidate()
    }
}
