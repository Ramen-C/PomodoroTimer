package com.example.pomodorotimer.model

class TimerModel(
    private var workTime: Int = 25 * 60,  // 25分钟工作时间
    private val shortBreakTime: Int = 5 * 60,  // 5分钟短休息
    private val longBreakTime: Int = 20 * 60   // 20分钟长休息
) {
    var timeLeft = workTime
    var isWorkingState = true
    var cyclesCompleted = 0
    private var shortBreakCount = 0

    fun resetTimer() {
        timeLeft = if (isWorkingState) workTime else getCurrentBreakTime()
    }

    private fun getCurrentBreakTime(): Int {
        return if (shortBreakCount == 3) longBreakTime else shortBreakTime
    }

    fun toggleWorkRestCycle() {
        isWorkingState = !isWorkingState
        if (!isWorkingState) {
            // 进入休息状态
            cyclesCompleted++
            if (shortBreakCount < 3) {
                shortBreakCount++
                timeLeft = shortBreakTime
            } else {
                // 第四次休息，触发长休息
                shortBreakCount = 0
                timeLeft = longBreakTime
            }
        } else {
            // 返回工作状态
            timeLeft = workTime
        }
    }

    fun updateWorkTime(newWorkTime: Int) {
        workTime = newWorkTime
        if (isWorkingState) {
            timeLeft = workTime
        }
    }

    // 获取当前状态信息
    fun getCurrentCycleInfo(): String {
        return if (isWorkingState) {
            "工作中 (${shortBreakCount}/3)"
        } else {
            if (shortBreakCount == 3) "长休息" else "短休息 (${shortBreakCount}/3)"
        }
    }
}