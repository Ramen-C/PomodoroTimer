// TimerService.kt
package com.example.pomodorotimer.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import com.example.pomodorotimer.R
import com.example.pomodorotimer.model.TimerModel

/**
 * TimerService现在使用TimerModel进行计时逻辑。
 * Service作为后台运行的载体，不再直接维护timeLeft与isRunning，而是通过TimerModel获取与更新。
 * 这样逻辑统一由TimerModel管理，TimerService仅在后台执行Model的变更并通过通知更新UI状态。
 */
class TimerService : Service() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "pomodoro_channel_id"
        const val NOTIFICATION_ID = 1
        const val ACTION_TIMER_FINISHED = "com.example.pomodorotimer.TIMER_FINISHED"
    }

    private val binder = LocalBinder()

    // 使用TimerModel作为计时数据来源
    private val timerModel = TimerModel()
    private var job: Job? = null
    private var isRunning = false

    fun getTimeLeft() = timerModel.timeLeft
    fun isTimerRunning() = isRunning

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForegroundServiceNotification()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    // 启动定时器：通过协程后台递减TimerModel中的timeLeft
    fun startTimer() {
        if (isRunning) return
        isRunning = true
        job = CoroutineScope(Dispatchers.IO).launch {
            while (timerModel.timeLeft > 0 && isRunning) {
                delay(1000)
                timerModel.timeLeft -= 1
                withContext(Dispatchers.Main) {
                    updateNotification()
                }
            }
            if (timerModel.timeLeft == 0) {
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        isRunning = false
        job?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        timerModel.resetTimer()
        updateNotification()
    }

    // 定时器完成后发送广播，由Receiver或UI层处理事件提醒用户
    private fun onTimerFinished() {
        isRunning = false
        val intent = Intent(ACTION_TIMER_FINISHED)
        sendBroadcast(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Pomodoro Timer Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun startForegroundServiceNotification() {
        val notification = buildNotification("计时中", formatTime(timerModel.timeLeft))
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun updateNotification() {
        val notification = buildNotification("计时中", formatTime(timerModel.timeLeft))
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildNotification(title: String, text: String): Notification {
        // 点击通知返回MainActivity
        val mainIntent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText("剩余时间: $text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return String.format("%02d:%02d", m, s)
    }

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }
}
