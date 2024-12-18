// EditTaskDialog.kt
package com.example.pomodorotimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.pomodorotimer.controller.TaskController
import com.example.pomodorotimer.model.Task

@Composable
fun EditTaskDialog(
    task: Task?,
    taskController: TaskController,
    onDismiss: () -> Unit,
    onSaveComplete: () -> Unit,
    onDeleteComplete: () -> Unit
) {
    var name by remember { mutableStateOf(task?.name ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (task == null) "Create New Task" else "Edit Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank()) {
                    val newTask = Task(
                        id = task?.id ?: 0L,
                        name = name,
                        description = description
                    )
                    taskController.saveTask(newTask, onSaveComplete)  // 传递回调
                    onDismiss()  // 关闭对话框
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Row {
                if (task != null) {
                    TextButton(onClick = {
                        taskController.deleteTask(task, onDeleteComplete)  // 传递回调
                        onDismiss()  // 关闭对话框
                    }) {
                        Text("Delete")
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}
