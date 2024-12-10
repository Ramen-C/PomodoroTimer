// Cycle.kt
package com.example.pomodorotimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cycles")
data class Cycle(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long, // 存储完成周期的时间戳（毫秒）
    val taskId: Long // 关联的任务ID
)
