package com.craneradius.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CraneColors = darkColorScheme(
    primary = Color(0xFFFFC107),
    onPrimary = Color(0xFF1A1A1A),
    primaryContainer = Color(0xFF4A3A00),
    onPrimaryContainer = Color(0xFFFFE082),
    secondary = Color(0xFF90A4AE),
    onSecondary = Color(0xFF1A1A1A),
    secondaryContainer = Color(0xFF2E3A40),
    onSecondaryContainer = Color(0xFFCFD8DC),
    background = Color(0xFF0D1117),
    onBackground = Color(0xFFECEFF1),
    surface = Color(0xFF161B22),
    onSurface = Color(0xFFECEFF1),
    surfaceVariant = Color(0xFF21262D),
    onSurfaceVariant = Color(0xFFB0BEC5),
    error = Color(0xFFEF5350),
    onError = Color(0xFF1A1A1A),
    outline = Color(0xFF546E7A)
)

@Composable
fun CraneRadiusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CraneColors,
        content = content
    )
}
