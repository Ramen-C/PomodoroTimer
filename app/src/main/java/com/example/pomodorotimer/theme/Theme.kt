package com.example.pomodorotimer.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.pomodorotimer.controller.AppTheme

private val RedThemeColors = lightColorScheme(
    primary = RedPrimary,
    onPrimary = RedOnPrimary,
    background = RedBackground,
    surface = RedSurface,
    onSurface = Color.Black
)

private val BlueThemeColors = lightColorScheme(
    primary = Color(0xFF2196F3),
    onPrimary = Color.White,
    background = Color(0xFFE3F2FD),
    surface = Color(0xFFBBDEFB),
    onSurface = Color.Black
)

private val GreenThemeColors = lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.White,
    background = Color(0xFFE8F5E9),
    surface = Color(0xFFC8E6C9),
    onSurface = Color.Black
)

fun getColorSchemeForTheme(theme: AppTheme) = when(theme) {
    AppTheme.RED -> RedThemeColors
    AppTheme.BLUE -> BlueThemeColors
    AppTheme.GREEN -> GreenThemeColors
}

@Composable
fun PomodoroTimerTheme(
    theme: AppTheme,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = getColorSchemeForTheme(theme),
        typography = Typography,
        content = content
    )
}
