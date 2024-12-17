package com.example.pomodorotimer.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.pomodorotimer.service.TimerService

/**
 * TimerReceiver可视为View层的一部分：接收后台的计时完成事件并对用户进行反馈(Toast)。
 * 如需进一步处理，可在这里启动Activity或通知Controller，但为保持MVC清晰，此处仅作为简单提示UI。
 */
class TimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TimerService.ACTION_TIMER_FINISHED) {
            // 简单的UI提示用户计时结束
            Toast.makeText(context, "计时完成！", Toast.LENGTH_SHORT).show()
            // 如需进一步控制逻辑，可在此启动Activity或发送intent给UI层组件
        }
    }
}
