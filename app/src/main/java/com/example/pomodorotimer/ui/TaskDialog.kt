package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.model.Task
import com.example.pomodorotimer.model.TaskViewModel

@Composable
fun TaskDialog(
    taskViewModel: TaskViewModel,
    onDismiss: () -> Unit,
    onTaskSelected: (Task) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var currentTask by remember { mutableStateOf<Task?>(null) }

    if (showEditDialog) {
        EditTaskDialog(
            task = currentTask,
            onDismiss = { showEditDialog = false },
            onSave = { task ->
                taskViewModel.insertTask(task)
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
                    taskViewModel = taskViewModel,
                    onTaskSelected = { task ->
                        onTaskSelected(task)
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
    taskViewModel: TaskViewModel,
    onTaskSelected: (Task) -> Unit
) {
    val taskList by taskViewModel.allTasks.observeAsState(emptyList())

    if (taskList.isEmpty()) {
        Text("没有可选择的任务", style = MaterialTheme.typography.bodyLarge)
    } else {
        LazyColumn {
            items(taskList.size) { index ->
                val task = taskList[index]
                TaskItem(
                    task = task,
                    onClick = {
                        onTaskSelected(task)
                    }
                )
            }
        }
    }
}
