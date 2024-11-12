package com.example.pomodorotimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class TimerViewModel : ViewModel() {
    var timeLeft by mutableStateOf(25 * 60) // 25 分钟，单位为秒
        private set

    var isRunning by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    val timerFinishedEvent = MutableLiveData<Unit>()

    fun startTimer() {
        if (isRunning) return
        isRunning = true
        timerJob = viewModelScope.launch {
            while (timeLeft > 0 && isRunning) {
                delay(1000L)
                timeLeft -= 1
            }
            if (timeLeft == 0) {
                isRunning = false
                timerFinishedEvent.postValue(Unit)
            }
        }
    }

    fun pauseTimer() {
        isRunning = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        timeLeft = 25 * 60 // 重置为 25 分钟
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
