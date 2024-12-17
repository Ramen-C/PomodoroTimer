package com.example.pomodorotimer.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = RedPrimary,
    onPrimary = RedOnPrimary,
    background = RedBackground,
    surface = RedSurface,
    onSurface = Color.Black
)

@Composable
fun PomodoroTimerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}