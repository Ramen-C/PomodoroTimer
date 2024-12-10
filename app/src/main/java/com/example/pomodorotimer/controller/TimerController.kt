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

    // 添加刷新时间的方法
    fun refreshTime() {
        _timeLeft.value = timerModel.timeLeft
        _cycleInfo.value = timerModel.getCurrentCycleInfo()
    }

    // 新增方法：获取当前工作和休息时间（分钟）
    fun getCurrentWorkTimeInMinutes(): Int = timerModel.getCurrentWorkTime() / 60
    fun getCurrentShortBreakTimeInMinutes(): Int = timerModel.getCurrentShortBreakTime() / 60
    fun getCurrentLongBreakTimeInMinutes(): Int = timerModel.getCurrentLongBreakTime() / 60

    // 新增方法：更新休息时间（分钟）
    fun updateShortBreakTime(newShortBreakTimeInMinutes: Int) {
        timerModel.updateShortBreakTime(newShortBreakTimeInMinutes)
        _timeLeft.value = timerModel.timeLeft
    }

    fun updateLongBreakTime(newLongBreakTimeInMinutes: Int) {
        timerModel.updateLongBreakTime(newLongBreakTimeInMinutes)
        _timeLeft.value = timerModel.timeLeft
    }
}
