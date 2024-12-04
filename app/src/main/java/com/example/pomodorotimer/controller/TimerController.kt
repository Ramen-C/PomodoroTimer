package com.example.pomodorotimer.controller
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.model.Task
import com.example.pomodorotimer.model.TimerModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerController : ViewModel() {
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
        viewModelScope.launch(Dispatchers.IO) {
            // 这里可以调用 TaskController 或直接访问数据层来记录进度
            // 例如记录完成的番茄周期数、总时长等
        }
    }
}