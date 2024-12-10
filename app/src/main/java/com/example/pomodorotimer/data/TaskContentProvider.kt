package com.example.pomodorotimer.data

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import com.example.pomodorotimer.model.Task

class TaskContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.pomodorotimer.provider"
        const val TASKS_TABLE = "tasks"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$TASKS_TABLE")

        // UriMatcher 的匹配码
        private const val TASKS = 1
        private const val TASK_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TASKS_TABLE, TASKS)
            addURI(AUTHORITY, "$TASKS_TABLE/#", TASK_ID)
        }
    }

    private lateinit var taskDao: TaskDao

    override fun onCreate(): Boolean {
        context?.let {
            taskDao = AppDatabase.getDatabase(it).taskDao()
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            TASKS -> {
                val cursor = taskDao.getTasksCursor()
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            TASK_ID -> {
                val id = ContentUris.parseId(uri)
                val cursor = taskDao.getTaskByIdCursor(id)
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            else -> throw IllegalArgumentException("未知的 URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            TASKS -> "vnd.android.cursor.dir/$AUTHORITY.$TASKS_TABLE"
            TASK_ID -> "vnd.android.cursor.item/$AUTHORITY.$TASKS_TABLE"
            else -> throw IllegalArgumentException("未知的 URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (uriMatcher.match(uri)) {
            TASKS -> {
                val task = Task.fromContentValues(values)
                val id = taskDao.insertTaskSync(task)
                if (id != -1L) {
                    val insertedUri = ContentUris.withAppendedId(CONTENT_URI, id)
                    context?.contentResolver?.notifyChange(insertedUri, null)
                    insertedUri
                } else {
                    throw SQLException("插入行失败：$uri")
                }
            }
            else -> throw IllegalArgumentException("未知的 URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (uriMatcher.match(uri)) {
            TASK_ID -> {
                val id = ContentUris.parseId(uri)
                val task = Task(id, "", "")
                val count = taskDao.deleteTaskSync(task)
                if (count > 0) {
                    context?.contentResolver?.notifyChange(uri, null)
                }
                count
            }
            else -> throw IllegalArgumentException("未知的 URI: $uri")
        }
    }


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            TASK_ID -> {
                val id = ContentUris.parseId(uri)
                val task = Task.fromContentValues(values).copy(id = id)
                val count = taskDao.updateTaskSync(task)
                if (count > 0) {
                    context?.contentResolver?.notifyChange(uri, null)
                }
                count
            }
            else -> throw IllegalArgumentException("未知的 URI: $uri")
        }
    }
}
