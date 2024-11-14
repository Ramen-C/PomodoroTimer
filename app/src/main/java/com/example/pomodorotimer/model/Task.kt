// 文件：Task.kt
package com.example.pomodorotimer.model

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = ""
) {
    companion object {
        fun fromContentValues(values: ContentValues?): Task {
            var id = 0L
            var name = ""
            var description = ""
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
            }
            return Task(id, name, description)
        }
    }
}
