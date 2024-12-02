package com.example.pomodorotimer.controller
import androidx.lifecycle.ViewModel
import com.example.pomodorotimer.model.Task
import com.example.pomodorotimer.model.TimerModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerController : ViewModel() {
    private val timerModel = TimerModel()
    private var timerJob: Job? = null

    // 使用 StateFlow 来管理状态
    private val _timeLeft = MutableStateFlow(timerModel.timeLeft)
    val timeLeft: StateFlow<Int> get() = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> get() = _isRunning

    private val _isWorking = MutableStateFlow(timerModel.isWorkingState)
    val isWorking: StateFlow<Boolean> get() = _isWorking

    var currentTask: Task? = null

    // 启动计时器
    fun startTimer() {
        if (_isRunning.value) return
        _isRunning.value = true

        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (_timeLeft.value > 0 && _isRunning.value) {
                delay(1000L)
                _timeLeft.value -= 1
            }
            if (_timeLeft.value == 0) {
                onTimerComplete()
            }
        }
    }

    // 暂停计时器
    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    // 重置计时器
    fun resetTimer() {
        pauseTimer()
        timerModel.resetTimer()
        _timeLeft.value = timerModel.timeLeft
    }

    // 切换工作与休息周期
    fun toggleWorkRestCycle() {
        timerModel.toggleWorkRestCycle()
        _isWorking.value = timerModel.isWorkingState
        _timeLeft.value = timerModel.timeLeft
    }

    private fun onTimerComplete() {
        _isRunning.value = false
        toggleWorkRestCycle()  // 切换工作/休息状态
    }

    // 更新工作时间
    fun updateWorkTime(newWorkTime: Int) {
        timerModel.updateWorkTime(newWorkTime)
        _timeLeft.value = newWorkTime
    }
}
