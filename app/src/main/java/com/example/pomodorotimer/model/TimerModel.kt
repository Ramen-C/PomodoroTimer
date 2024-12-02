package com.example.pomodorotimer.model

class TimerModel(private var workTime: Int = 25 * 60, private val breakTime: Int = 5 * 60) {
    var timeLeft = workTime  // 当前剩余时间
    var isWorkingState = true  // 当前状态：工作或休息
    var cyclesCompleted = 0  // 计时器循环次数

    fun resetTimer() {
        timeLeft = if (isWorkingState) workTime else breakTime
    }

    fun toggleWorkRestCycle() {
        isWorkingState = !isWorkingState
        cyclesCompleted += 1
        timeLeft = if (isWorkingState) workTime else breakTime
    }

    fun updateWorkTime(newWorkTime: Int) {
        workTime = newWorkTime
        if (isWorkingState) {
            timeLeft = workTime
        }
    }
}
