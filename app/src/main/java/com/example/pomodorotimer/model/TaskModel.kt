package com.example.pomodorotimer.model

import android.database.Cursor
import com.example.pomodorotimer.data.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskModel(private val taskDao: TaskDao) {

    // 获取所有任务，直接返回Cursor数据（避免使用LiveData）
    suspend fun getAllTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            val cursor: Cursor = taskDao.getTasksCursor()
            val tasks = mutableListOf<Task>()
            while (cursor.moveToNext()) {
                val task = Task.fromCursor(cursor)  // 使用 fromCursor 方法
                tasks.add(task)
            }
            cursor.close()
            tasks
        }
    }

    // 获取单个任务
    suspend fun getTaskById(id: Long): Task? {
        return withContext(Dispatchers.IO) {
            val cursor: Cursor = taskDao.getTaskByIdCursor(id)
            var task: Task? = null
            if (cursor.moveToNext()) {
                task = Task.fromCursor(cursor)  // 使用 fromCursor 方法
            }
            cursor.close()
            task
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
}
