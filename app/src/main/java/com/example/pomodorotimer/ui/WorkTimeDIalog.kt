package com.example.pomodorotimer.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WorkTimeDialog(
    initialWorkTime: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var workTime by remember {mutableStateOf(initialWorkTime) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("设置工作时间") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("设置工作时间：$workTime 分钟")
                Slider(
                    value = workTime.toFloat(),
                    onValueChange = { workTime = it.toInt() },
                    valueRange = 1f..60f,
                    steps = 59,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(workTime) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}
