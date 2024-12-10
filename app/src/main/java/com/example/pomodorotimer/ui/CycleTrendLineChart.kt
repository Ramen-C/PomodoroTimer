// CycleTrendLineChart.kt
package com.example.pomodorotimer.ui

import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.example.pomodorotimer.model.CycleCount

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
        val dataSet = LineDataSet(entries, "番茄周期完成趋势").apply {
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
