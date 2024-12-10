// TaskController.kt
package com.example.pomodorotimer.controller

import com.example.pomodorotimer.model.Cycle
import com.example.pomodorotimer.model.Task
import com.example.pomodorotimer.model.CycleCount
import com.example.pomodorotimer.model.TaskModel
import com.example.pomodorotimer.model.TaskTimeStat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskController(private val taskModel: TaskModel) {

    // 获取所有任务
    fun getAllTasks(onResult: (List<Task>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val tasks = taskModel.getAllTasks()
            onResult(tasks)
        }
    }

    // 保存任务（插入或更新）
    fun saveTask(task: Task, onSaveComplete: () -> Unit, onError: (String) -> Unit = {}) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (task.id == 0L) {
                    taskModel.insertTask(task)
                } else {
                    taskModel.updateTask(task)
                }
                onSaveComplete() // 操作完成后回调
            } catch (e: Exception) {
                onError(e.message ?: "未知错误")
            }
        }
    }

    // 删除任务
    fun deleteTask(task: Task, onDeleteComplete: () -> Unit, onError: (String) -> Unit = {}) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                taskModel.deleteTask(task)
                onDeleteComplete() // 操作完成后回调
            } catch (e: Exception) {
                onError(e.message ?: "未知错误")
            }
        }
    }

    // 更新任务列表
    fun updateTasks(onUpdateComplete: (List<Task>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val tasks = taskModel.getAllTasks() // 获取最新的任务列表
            onUpdateComplete(tasks) // 回调更新任务列表
        }
    }

    // 更新任务累计时间
    fun updateTaskTime(task: Task, additionalTime: Long, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val updatedTask = task.copy(totalTimeSpent = task.totalTimeSpent + additionalTime)
            taskModel.updateTask(updatedTask)
            onComplete() // 操作完成后回调
        }
    }

    // 记录一个完成的周期
    fun recordCycle(task: Task, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val cycle = Cycle(
                timestamp = System.currentTimeMillis(),
                taskId = task.id
            )
            taskModel.insertCycle(cycle)
            onComplete() // 操作完成后回调
        }
    }

    // 获取任务时间统计
    fun getTaskTimeStats(onResult: (List<TaskTimeStat>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val taskTimeStats = taskModel.getTaskTimeStats()
            onResult(taskTimeStats)
        }
    }

    // 获取每日番茄周期数
    fun getCyclesPerDay(onResult: (List<CycleCount>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val cycles = taskModel.getCyclesPerDay()
            onResult(cycles)
        }
    }

    // 获取番茄周期完成趋势
    fun getCyclesTrend(onResult: (List<CycleCount>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val trend = taskModel.getCyclesTrend()
            onResult(trend)
        }
    }

    // 获取工作时间分布
    fun getWorkTimeDistribution(onResult: (Map<String, Map<String, Int>>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val distribution = taskModel.getWorkTimeDistribution()
            onResult(distribution)
        }
    }

    // 获取Flow形式的数据，用于ProgressScreen
    fun getTaskTimeStatsFlow(): Flow<List<TaskTimeStat>> {
        return taskModel.getTaskTimeStatsFlow()
    }

    fun getCyclesPerDayFlow(): Flow<List<CycleCount>> {
        return taskModel.getCyclesPerDayFlow()
    }

    fun getCyclesTrendFlow(): Flow<List<CycleCount>> {
        return taskModel.getCyclesTrendFlow()
    }
}
