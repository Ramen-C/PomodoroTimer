// TimerController.kt
package com.example.pomodorotimer.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.model.CycleType
import com.example.pomodorotimer.model.Task
import com.example.pomodorotimer.model.TimerModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerController(private val taskController: TaskController) : ViewModel() {
    val timerModel = TimerModel()
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

    //摇晃检测状态
    private val _isShakeToPauseEnabled = MutableStateFlow(false)
    val isShakeToPauseEnabled: StateFlow<Boolean> get() = _isShakeToPauseEnabled

    // 提示显示状态
    private val _promptShow = MutableStateFlow(false)
    val promptShow: StateFlow<Boolean> get() = _promptShow

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
        _promptShow.value = false // 重置提示状态
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

        // 在切换周期之前，获取刚完成的周期类型
        val completedCycleType = timerModel.getCurrentCycleType()

        // 切换周期
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

        when {
            // 自动模式下，完成一轮工作（长休息结束）
            _isAutoMode.value && completedCycleType == CycleType.LongBreak -> {
                _promptShow.value = true
            }
            // 手动模式下，任何阶段结束后都弹出提示
            !_isAutoMode.value -> {
                _promptShow.value = true
            }
            else -> {
                // 自动模式下，非长休息阶段，继续下一个周期
                startTimer()
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

    // 重置提示状态
    fun resetPromptShow() {
        _promptShow.value = false
    }

    // 新增方法：判断当前是否是长休息
    fun isCurrentLongBreak(): Boolean {
        return timerModel.isLongBreak()
    }

    //摇晃检测
    fun setShakeToPauseEnabled(enabled: Boolean) {
        _isShakeToPauseEnabled.value = enabled
    }

    private val _currentTheme = MutableStateFlow(AppTheme.RED)
    val currentTheme: StateFlow<AppTheme> get() = _currentTheme

    fun setTheme(theme: AppTheme) {
        _currentTheme.value = theme
    }
}
