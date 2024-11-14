package com.example.pomodorotimer.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class TimerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TimerReceiver", "Received broadcast: ${intent.action}")

        // 创建通知通道（仅适用于 Android 8.0 及以上）
        createNotificationChannel(context)

        // 创建并发送通知
        val notification = NotificationCompat.Builder(context, "PomodoroChannel")
            .setContentTitle("番茄钟计时结束")
            .setContentText("休息一下吧！")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 500, 500, 500))  // 振动
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    // 创建通知通道（适用于 Android 8.0 及以上）
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "PomodoroChannel"
            val channelName = "Pomodoro Timer Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = "用于番茄钟计时结束的通知"

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
