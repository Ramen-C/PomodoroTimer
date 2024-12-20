// CycleTrendLineChart.kt
package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.pomodorotimer.model.CycleCount
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun CycleTrendLineChart(cyclesTrend: List<CycleCount>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                axisLeft.axisMinimum = 0f
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f
                xAxis.labelRotationAngle = -45f
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) { lineChart ->
        if (cyclesTrend.isEmpty()) {
            lineChart.clear()
            lineChart.invalidate()
            return@AndroidView
        }
        val sortedTrend = cyclesTrend.sortedBy { it.date }
        val entries = sortedTrend.mapIndexed { index, cycleCount ->
            Entry(index.toFloat(), cycleCount.count.toFloat())
        }
        val dataSet = LineDataSet(entries, "Pomodoro Cycle Completion Trend").apply {
            color = AndroidColor.BLUE
            lineWidth = 2f
            setDrawCircles(true)
            circleRadius = 4f
            valueTextSize = 12f
            setDrawValues(true)
        }
        val data = LineData(dataSet)
        lineChart.data = data
        lineChart.xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(sortedTrend.map { it.date })
        lineChart.invalidate()
    }
}
