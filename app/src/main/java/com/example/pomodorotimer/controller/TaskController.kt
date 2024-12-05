// TaskController.kt
package com.example.pomodorotimer.controller

import com.example.pomodorotimer.model.Task
import com.example.pomodorotimer.model.TaskModel
import com.example.pomodorotimer.model.TaskTimeStat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskController(private val taskModel: TaskModel) {

    // 获取所有任务
    fun getAllTasks(onResult: (List<Task>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val tasks = taskModel.getAllTasks()
            onResult(tasks)
        }
    }

    // 获取单个任务
    fun getTaskById(id: Long, onResult: (Task?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val task = taskModel.getTaskById(id)
            onResult(task)
        }
    }

    // 保存任务（插入或更新）
    fun saveTask(task: Task, onSaveComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            if (task.id == 0L) {
                taskModel.insertTask(task)
            } else {
                taskModel.updateTask(task)
            }
            onSaveComplete() // 操作完成后回调
        }
    }

    // 删除任务
    fun deleteTask(task: Task, onDeleteComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            taskModel.deleteTask(task)
            onDeleteComplete() // 操作完成后回调
        }
    }

    // 更新任务列表
    fun updateTasks(onUpdateComplete: (List<Task>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val tasks = taskModel.getAllTasks() // 获取最新的任务列表
            onUpdateComplete(tasks) // 回调更新任务列表
        }
    }

    // 新增：获取任务时间统计
    fun getTaskTimeStats(onResult: (List<TaskTimeStat>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val taskTimeStats = taskModel.getTaskTimeStats()
            onResult(taskTimeStats) // 回调任务统计数据
        }
    }

    // 新增：更新任务累计时间
    fun updateTaskTime(task: Task, additionalTime: Long, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val updatedTask = task.copy(totalTimeSpent = task.totalTimeSpent + additionalTime)
            taskModel.updateTask(updatedTask)
            onComplete() // 操作完成后回调
        }
    }
}
