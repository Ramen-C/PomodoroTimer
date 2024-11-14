package com.example.pomodorotimer.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.pomodorotimer.model.Task


@Composable
fun EditTaskDialog(
    task: Task?,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    onDelete: ((Task) -> Unit)? = null
) {
    var name by remember { mutableStateOf(task?.name ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (task == null) "新建任务" else "编辑任务") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("任务名称") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("任务描述") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank()) {
                    val newTask = Task(
                        id = task?.id ?: 0,
                        name = name,
                        description = description
                    )
                    onSave(newTask)
                }
            }) {
                Text("保存")
            }
        },
        dismissButton = {
            Row {
                if (task != null && onDelete != null) {
                    TextButton(onClick = { onDelete(task) }) {
                        Text("删除")
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        }
    )
}
