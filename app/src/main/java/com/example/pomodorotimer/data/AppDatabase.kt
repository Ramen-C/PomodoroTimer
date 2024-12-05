// AppDataBase.kt
package com.example.pomodorotimer.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pomodorotimer.model.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                ).build().also { instance = it }
            }

        // 添加删除数据库的方法
        fun deleteDatabase(context: Context) {
            synchronized(this) {
                context.deleteDatabase("app_database") // 删除数据库文件
                instance = null // 确保实例被重置
            }
        }
    }
}
