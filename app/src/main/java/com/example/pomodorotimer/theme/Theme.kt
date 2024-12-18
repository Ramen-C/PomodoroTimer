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
    primary = BluePrimary,
    onPrimary = BlueOnPrimary,
    background = BlueBackground,
    surface = BlueSurface,
    onSurface = Color.Black
)

private val GreenThemeColors = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = GreenOnPrimary,
    background = GreenBackground,
    surface = GreenSurface,
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
