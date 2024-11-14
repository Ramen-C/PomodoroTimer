// 文件：TaskDao.kt
package com.example.pomodorotimer.data

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pomodorotimer.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks")
    fun getTasksCursor(): Cursor

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskByIdCursor(id: Long): Cursor

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
