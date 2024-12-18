// CycleDao.kt
package com.example.pomodorotimer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pomodorotimer.model.Cycle
import com.example.pomodorotimer.model.CycleCount
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Insert
    suspend fun insertCycle(cycle: Cycle)

    @Query("""
        SELECT DATE(timestamp / 1000, 'unixepoch') as date, COUNT(*) as count 
        FROM cycles 
        GROUP BY DATE(timestamp / 1000, 'unixepoch') 
        ORDER BY date DESC
    """)

    fun getCyclesPerDayFlow(): Flow<List<CycleCount>>

    @Query("""
        SELECT DATE(timestamp / 1000, 'unixepoch') as date, COUNT(*) as count 
        FROM cycles 
        GROUP BY DATE(timestamp / 1000, 'unixepoch') 
        ORDER BY date ASC
    """)
    fun getCyclesTrendFlow(): Flow<List<CycleCount>>
}
