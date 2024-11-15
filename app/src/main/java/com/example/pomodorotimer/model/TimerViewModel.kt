// 文件：TimerViewModel.kt
package com.example.pomodorotimer.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class TimerViewModel : ViewModel() {
    var workTime by mutableIntStateOf(25 * 60)  // 默认 25 分钟
    private var breakTime by mutableIntStateOf(5 * 60)  // 默认 5 分钟

    var timeLeft by mutableIntStateOf(workTime)  // 当前剩余时间
        private set

    var isRunning by mutableStateOf(false)
        private set

    private var isWorkingState by mutableStateOf(true)  // 当前状态：工作或休息
    val isWorking: Boolean
        get() = isWorkingState  // 通过 getter 公开 isWorking

    private var timerJob: Job? = null
    private var cyclesCompleted by mutableIntStateOf(0)  // 计时器循环次数

    val timerFinishedEvent = MutableLiveData<Unit>()
    private val updateTimeEvent = MutableLiveData<Unit>()

    // 更新工作时间
    fun updateWorkTime(newWorkTime: Int) {
        workTime = newWorkTime
        timeLeft = newWorkTime  // 更新当前剩余时间
    }

    fun startTimer() {
        if (isRunning) return
        isRunning = true
        // 启动新的计时任务，使用当前工作或休息时间
        timerJob = viewModelScope.launch {
            while (timeLeft > 0 && isRunning) {
                delay(1000L)
                timeLeft -= 1
                updateTimeEvent.postValue(Unit)  // 实时更新UI
            }
            if (timeLeft == 0) {
                onTimerComplete()
            }
        }
    }

    // 暂停计时器
    fun pauseTimer() {
        isRunning = false
        timerJob?.cancel()
    }

    // 重置计时器
    fun resetTimer() {
        pauseTimer()
        timeLeft = if (isWorking) workTime else breakTime
    }

    // 切换工作与休息周期
    private fun toggleWorkRestCycle() {
        isWorkingState = !isWorkingState
        cyclesCompleted += 1
        timeLeft = if (isWorking) workTime else breakTime
    }

    // 计时器完成后的操作
    private fun onTimerComplete() {
        isRunning = false
        toggleWorkRestCycle()  // 切换工作/休息状态
        timerFinishedEvent.postValue(Unit)  // 发送计时结束事件
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    var currentTask by mutableStateOf<Task?>(null)
}
