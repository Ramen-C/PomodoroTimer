// TaskDialog.kt
package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.model.Task

@Composable
fun TaskDialog(
    taskController: TaskController,
    onDismiss: () -> Unit,
    onTaskSelected: (Task) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var currentTask by remember { mutableStateOf<Task?>(null) }
    var taskList by remember { mutableStateOf<List<Task>>(emptyList()) }

    // 加载任务列表
    LaunchedEffect(true) {
        taskController.getAllTasks { tasks ->
            taskList = tasks
        }
    }

    // 显示编辑对话框（新建或编辑）
    if (showEditDialog) {
        EditTaskDialog(
            task = currentTask,
            taskController = taskController,
            onDismiss = { showEditDialog = false },
            onSaveComplete = {
                taskController.updateTasks { tasks ->
                    taskList = tasks
                }
                showEditDialog = false
            },
            onDeleteComplete = {
                taskController.updateTasks { tasks ->
                    taskList = tasks
                }
                showEditDialog = false
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择任务") },
        text = {
            Column {
                TaskList(
                    tasks = taskList,
                    onTaskSelected = { task ->
                        onTaskSelected(task)
                    },
                    onTaskLongPressed = { task ->
                        currentTask = task
                        showEditDialog = true
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        currentTask = null
                        showEditDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("新建任务")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskSelected: (Task) -> Unit,
    onTaskLongPressed: (Task) -> Unit // 新增回调，用于长按
) {
    if (tasks.isEmpty()) {
        Text("没有可选择的任务", style = MaterialTheme.typography.bodyLarge)
    } else {
        LazyColumn {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onClick = { onTaskSelected(task) },
                    onLongClick = { onTaskLongPressed(task) } // 将长按事件传出
                )
            }
        }
    }
}
