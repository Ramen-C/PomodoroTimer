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

    if (showDialog) {
        // 添加缺少的回调函数 onSaveComplete 和 onDeleteComplete
        EditTaskDialog(
            task = currentTask,
            taskController = taskController,
            onSaveComplete = {
                showDialog = false // 保存完成后，关闭对话框
                // 这里可以根据需要刷新任务列表或进行其他操作
            },
            onDeleteComplete = {
                showDialog = false // 删除完成后，关闭对话框
                // 这里可以根据需要刷新任务列表或进行其他操作
            },
            onDismiss = { showDialog = false } // 取消操作
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("任务管理") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                currentTask = null
                showDialog = true
            }) {
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
                        currentTask = task
                        showDialog = true
                    }
                )
            }
        }
    }
}
