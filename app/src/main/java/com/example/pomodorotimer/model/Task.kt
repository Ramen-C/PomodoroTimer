package com.example.pomodorotimer.model

import android.content.ContentValues
import android.database.Cursor
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val totalTimeSpent: Long = 0L
) {
    companion object {
        // 从 ContentValues 创建 Task 对象
        fun fromContentValues(values: ContentValues?): Task {
            var id = 0L
            var name = ""
            var description = ""
            var totalTimeSpent = 0L
            values?.let {
                if (it.containsKey("id")) {
                    id = it.getAsLong("id")
                }
                if (it.containsKey("name")) {
                    name = it.getAsString("name")
                }
                if (it.containsKey("description")) {
                    description = it.getAsString("description")
                }
                if (it.containsKey("totalTimeSpent")) {
                    totalTimeSpent = it.getAsLong("totalTimeSpent")
                }
            }
            return Task(id, name, description, totalTimeSpent)
        }

        // 从 Cursor 创建 Task 对象
        fun fromCursor(cursor: Cursor): Task {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val totalTimeSpent = cursor.getLong(cursor.getColumnIndexOrThrow("totalTimeSpent"))
            // 此处将 totalTimeSpent 正确传入 Task 构造函数
            return Task(id, name, description, totalTimeSpent)
        }
    }
}
