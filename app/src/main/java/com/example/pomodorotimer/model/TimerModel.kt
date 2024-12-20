// TimerModel.kt
package com.example.pomodorotimer.model

enum class CycleType {
    Work,
    ShortBreak,
    LongBreak
}

class TimerModel(
    private var workTime: Int = 25 * 60, // 默认25分钟工作时间（秒）
    private var shortBreakTime: Int = 5 * 60, // 默认5分钟短休息（秒）
    private var longBreakTime: Int = 20 * 60 // 默认20分钟长休息（秒）
) {
    var timeLeft = workTime
    var isWorkingState = true
        private set
    private var cyclesCompleted = 0
    private var shortBreakCount = 0

    fun resetTimer() {
        timeLeft = getInitialTime()
        // 重置短休息计数
        if (!isWorkingState) {
            shortBreakCount = 0
        }
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

    // 更新工作时间（分钟）
    fun updateWorkTime(newWorkTimeInMinutes: Int) {
        workTime = newWorkTimeInMinutes * 60
        if (isWorkingState) {
            timeLeft = workTime
        }
    }

    // 更新短休息时间（分钟）
    fun updateShortBreakTime(newShortBreakTimeInMinutes: Int) {
        shortBreakTime = newShortBreakTimeInMinutes * 60
        if (!isWorkingState && shortBreakCount < 3) {
            // 如果当前正处于短休息中，及时更新时间
            timeLeft = shortBreakTime
        }
    }

    // 更新长休息时间（分钟）
    fun updateLongBreakTime(newLongBreakTimeInMinutes: Int) {
        longBreakTime = newLongBreakTimeInMinutes * 60
        if (!isWorkingState && shortBreakCount == 0) {
            // 如果当前正处于长休息中，及时更新时间
            timeLeft = longBreakTime
        }
    }

    fun getCurrentWorkTime(): Int = workTime
    fun getCurrentShortBreakTime(): Int = shortBreakTime
    fun getCurrentLongBreakTime(): Int = longBreakTime

    // 获取当前状态信息
    fun getCurrentCycleInfo(): String {
        return if (isWorkingState) {
            "工作中 (${shortBreakCount}/3)"
        } else {
            if (shortBreakCount == 0) "长休息" else "短休息 (${shortBreakCount}/3)"
        }
    }

    // 判断当前是否是长休息
    fun isLongBreak(): Boolean {
        return !isWorkingState && timeLeft == longBreakTime
    }

    // 获取当前周期类型
    fun getCurrentCycleType(): CycleType {
        return if (isWorkingState) {
            CycleType.Work
        } else {
            if (shortBreakCount == 0) CycleType.LongBreak else CycleType.ShortBreak
        }
    }

    // 新增方法：获取当前周期的初始时间
    fun getInitialTime(): Int {
        return if (isWorkingState) workTime else getCurrentBreakTime()
    }
}
