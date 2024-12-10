// DailyCycleBarChart.kt
package com.example.pomodorotimer.ui

import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.example.pomodorotimer.model.CycleCount

@Composable
fun DailyCycleBarChart(cyclesPerDay: List<CycleCount>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setDrawGridBackground(false)
                axisLeft.axisMinimum = 0f
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f
                xAxis.labelRotationAngle = -45f
            }
        },
        modifier = modifier
    ) { barChart ->
        if (cyclesPerDay.isEmpty()) {
            barChart.clear()
            barChart.invalidate()
            return@AndroidView
        }
        val sortedCycles = cyclesPerDay.sortedBy { it.date }
        val entries = sortedCycles.mapIndexed { index, cycleCount ->
            BarEntry(index.toFloat(), cycleCount.count.toFloat())
        }
        val dataSet = BarDataSet(entries, "每日番茄周期数").apply {
            color = AndroidColor.BLUE
            valueTextColor = AndroidColor.BLACK
            valueTextSize = 12f
        }
        val data = BarData(dataSet)
        barChart.data = data
        barChart.xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(sortedCycles.map { it.date })
        barChart.invalidate()
    }
}
