// TaskScreen.kt
package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.model.Task

@ExperimentalMaterial3Api
@Composable
fun TaskScreen(
    taskController: TaskController,
    onTaskSelected: (Task) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var currentTask by remember { mutableStateOf<Task?>(null) }
    var taskList by remember { mutableStateOf<List<Task>>(emptyList()) }

    // 加载任务列表
    LaunchedEffect(true) {
        taskController.getAllTasks { tasks ->
            taskList = tasks
        }
    }
    // 显示编辑对话框
    if (showDialog) {
        EditTaskDialog(
            task = currentTask,
            taskController = taskController,
            onSaveComplete = {
                taskController.getAllTasks { tasks ->
                    taskList = tasks
                }
                showDialog = false // 保存后关闭对话框
            },
            onDeleteComplete = {
                taskController.getAllTasks { tasks ->
                    taskList = tasks
                }
                showDialog = false // 删除后关闭对话框
            },
            onDismiss = { showDialog = false } // 取消操作
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("任务管理", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    currentTask = null
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "新建任务")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(taskList.size) { index ->
                val task = taskList[index]
                TaskItem(
                    task = task,
                    onClick = { onTaskSelected(task) },
                    onLongClick = {
                        // 长按时更新 currentTask 并显示对话框
                        currentTask = task
                        showDialog = true
                    }
                )
            }
        }
    }
}
