// TimerController.kt
package com.example.pomodorotimer.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.model.Task
import com.example.pomodorotimer.model.TimerModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerController(private val taskController: TaskController) : ViewModel() {
    private val timerModel = TimerModel()
    private var timerJob: Job? = null
    private val _currentTask = MutableStateFlow<Task?>(null)
    val currentTask: StateFlow<Task?> = _currentTask

    private val _timeLeft = MutableStateFlow(timerModel.timeLeft)
    val timeLeft: StateFlow<Int> get() = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> get() = _isRunning

    private val _isWorking = MutableStateFlow(timerModel.isWorkingState)
    val isWorking: StateFlow<Boolean> get() = _isWorking

    private val _cycleInfo = MutableStateFlow(timerModel.getCurrentCycleInfo())
    val cycleInfo: StateFlow<String> get() = _cycleInfo

    // 自动/手动模式状态
    private val _isAutoMode = MutableStateFlow(false)
    val isAutoMode: StateFlow<Boolean> get() = _isAutoMode

    // 工作轮完成状态
    private val _workRoundCompleted = MutableStateFlow(false)
    val workRoundCompleted: StateFlow<Boolean> get() = _workRoundCompleted

    fun toggleAutoMode() {
        _isAutoMode.value = !_isAutoMode.value
    }

    fun startTimer() {
        if (_isRunning.value) return
        _isRunning.value = true
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0 && _isRunning.value) {
                kotlinx.coroutines.delay(1000L)
                _timeLeft.value -= 1
            }
            if (_timeLeft.value == 0) {
                onTimerComplete()
            }
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        timerModel.resetTimer()
        _timeLeft.value = timerModel.timeLeft
        _cycleInfo.value = timerModel.getCurrentCycleInfo()
        _workRoundCompleted.value = false // 重置工作轮完成状态
    }

    fun updateWorkTime(newWorkTime: Int) {
        timerModel.updateWorkTime(newWorkTime)
        _timeLeft.value = timerModel.timeLeft
    }

    fun setCurrentTask(task: Task) {
        _currentTask.value = task
        // 任务切换时记录上一个任务的完成情况
        _currentTask.value?.let { previousTask ->
            recordTaskProgress(previousTask)
        }
    }

    private fun onTimerComplete() {
        _isRunning.value = false
        timerModel.toggleWorkRestCycle()
        _isWorking.value = timerModel.isWorkingState
        _timeLeft.value = timerModel.timeLeft
        _cycleInfo.value = timerModel.getCurrentCycleInfo()

        // 记录任务进度
        if (_isWorking.value) {
            _currentTask.value?.let { task ->
                recordTaskProgress(task)
            }
        }

        // 检查是否完成了一轮工作
        if (timerModel.isLongBreak()) {
            // 长休息结束，完成一轮工作，停止计时
            _workRoundCompleted.value = true
        } else {
            if (_isAutoMode.value) {
                // 自动模式下完成后直接进入下一个周期的计时
                startTimer()
            } else {
                // 手动模式下在计时完成后等待用户手动开始，不调用 startTimer()，让用户手动点击开始
            }
        }
    }

    private fun recordTaskProgress(task: Task) {
        viewModelScope.launch {
            // 更新任务的总时间
            taskController.updateTaskTime(task, timerModel.getCurrentWorkTime().toLong()) {
                // 更新成功后，记录周期
                taskController.recordCycle(task) {
                    // 进一步的操作（如果需要）
                }
            }
        }
    }

    fun refreshTime() {
        _timeLeft.value = timerModel.timeLeft
        _cycleInfo.value = timerModel.getCurrentCycleInfo()
    }

    fun getCurrentWorkTimeInMinutes(): Int = timerModel.getCurrentWorkTime() / 60
    fun getCurrentShortBreakTimeInMinutes(): Int = timerModel.getCurrentShortBreakTime() / 60
    fun getCurrentLongBreakTimeInMinutes(): Int = timerModel.getCurrentLongBreakTime() / 60

    fun updateShortBreakTime(newShortBreakTimeInMinutes: Int) {
        timerModel.updateShortBreakTime(newShortBreakTimeInMinutes)
        _timeLeft.value = timerModel.timeLeft
    }

    fun updateLongBreakTime(newLongBreakTimeInMinutes: Int) {
        timerModel.updateLongBreakTime(newLongBreakTimeInMinutes)
        _timeLeft.value = timerModel.timeLeft
    }

    // 重置工作轮完成状态
    fun resetWorkRoundCompleted() {
        _workRoundCompleted.value = false
    }
}
