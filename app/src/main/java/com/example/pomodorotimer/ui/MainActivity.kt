package com.example.pomodorotimer.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.pomodorotimer.model.TimerViewModel
import com.example.pomodorotimer.model.TaskViewModel
import com.example.pomodorotimer.theme.PomodoroTimerTheme

class MainActivity : ComponentActivity() {
    private val timerViewModel: TimerViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 如果没有自定义的 enableEdgeToEdge()，请注释掉
        // enableEdgeToEdge()

        // 请求通知权限（适用于 Android 13 及以上）
        requestNotificationPermission()

        // 创建通知通道
        createNotificationChannel()

        setContent {
            PomodoroTimerTheme {
                TimerScreen(timerViewModel = timerViewModel, taskViewModel = taskViewModel)
            }
        }

        // 观察计时结束事件
        timerViewModel.timerFinishedEvent.observe(this) {
            showNotification()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_id"
            val channelName = "重要通知"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "这是一个用于重要通知的通道"
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (manager == null) {
            Log.e("NotificationTest", "NotificationManager is null")
            return
        }

        val notification = NotificationCompat.Builder(this, "my_channel_id")
            .setContentTitle("番茄钟计时结束")
            .setContentText("休息一下吧！")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        try {
            manager.notify(System.currentTimeMillis().toInt(), notification)
            Log.d("NotificationTest", "Notification sent successfully.")
        } catch (e: Exception) {
            Log.e("NotificationTest", "Error sending notification: ${e.message}")
        }
    }
}


