// TaskDao.kt
package com.example.pomodorotimer.data

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pomodorotimer.model.Task
import com.example.pomodorotimer.model.TaskTimeStat
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks")
    fun getTasksCursor(): Cursor

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskByIdCursor(id: Long): Cursor

    // 获取所有任务及其累计时间
    @Query("SELECT name, totalTimeSpent FROM tasks")
    fun getTaskTimeStats(): LiveData<List<TaskTimeStat>>

    @Query("SELECT name, totalTimeSpent FROM tasks")
    fun getTaskTimeStatsFlow(): Flow<List<TaskTimeStat>>

    @Insert
    suspend fun insertTask(task: Task)

    @Insert
    fun insertTaskSync(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Update
    fun updateTaskSync(task: Task): Int

    @Delete
    suspend fun deleteTask(task: Task)

    @Delete
    fun deleteTaskSync(task: Task): Int
}
