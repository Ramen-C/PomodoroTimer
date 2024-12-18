// TaskModel.kt
package com.example.pomodorotimer.model

import android.database.Cursor
import com.example.pomodorotimer.data.CycleDao
import com.example.pomodorotimer.data.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskModel(private val taskDao: TaskDao, private val cycleDao: CycleDao) {

    // 获取所有任务
    suspend fun getAllTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            val cursor: Cursor = taskDao.getTasksCursor()
            val tasks = mutableListOf<Task>()
            while (cursor.moveToNext()) {
                val task = Task.fromCursor(cursor)
                tasks.add(task)
            }
            cursor.close()
            tasks
        }
    }

    // 插入任务
    suspend fun insertTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }

    // 更新任务
    suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.updateTask(task)
        }
    }

    // 删除任务
    suspend fun deleteTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(task)
        }
    }


    // 插入周期
    suspend fun insertCycle(cycle: Cycle) {
        withContext(Dispatchers.IO) {
            cycleDao.insertCycle(cycle)
        }
    }

    // 使用 Flow 的统计数据获取方法
    fun getTaskTimeStatsFlow(): Flow<List<TaskTimeStat>> {
        return taskDao.getTaskTimeStatsFlow()
    }

    fun getCyclesPerDayFlow(): Flow<List<CycleCount>> {
        return cycleDao.getCyclesPerDayFlow()
    }

    fun getCyclesTrendFlow(): Flow<List<CycleCount>> {
        return cycleDao.getCyclesTrendFlow()
    }
}
