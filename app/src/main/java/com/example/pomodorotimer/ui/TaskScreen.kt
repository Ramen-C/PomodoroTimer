package com.example.pomodorotimer.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.pomodorotimer.model.Task
import com.example.pomodorotimer.model.TaskViewModel

@ExperimentalMaterial3Api
@Composable
fun TaskScreen(taskViewModel: TaskViewModel, onTaskSelected: (Task) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var currentTask by remember { mutableStateOf<Task?>(null) }

    if (showDialog) {
        EditTaskDialog(
            task = currentTask,
            onDismiss = { showDialog = false },
            onSave = { task ->
                if (currentTask == null) {
                    taskViewModel.insertTask(task)
                } else {
                    taskViewModel.updateTask(task)
                }
                showDialog = false
            },
            onDelete = { task ->
                taskViewModel.deleteTask(task)
                showDialog = false
            }
        )
    }

    val taskList by taskViewModel.allTasks.observeAsState(emptyList())

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
                    onClick = {
                        onTaskSelected(task)
                    },
                    onLongClick = {
                        currentTask = task
                        showDialog = true
                    }
                )
            }
        }
    }
}
