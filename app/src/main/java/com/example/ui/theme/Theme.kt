package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkCyberColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = SlateBase,
    primaryContainer = CyberCyanContainer,
    onPrimaryContainer = CyberCyanLight,
    secondary = CyberCyanLight,
    onSecondary = SlateBase,
    secondaryContainer = SlateCardSecondary,
    onSecondaryContainer = TextPrimary,
    tertiary = RiskEmerald,
    onTertiary = SlateBase,
    background = SlateBase,
    onBackground = TextPrimary,
    surface = SlateCard,
    onSurface = TextPrimary,
    surfaceVariant = SlateCardSecondary,
    onSurfaceVariant = TextSecondary,
    outline = SlateBorder,
    outlineVariant = SlateBorderSubtle,
    error = RiskCritical,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Dark-mode first design
    dynamicColor: Boolean = false, // Keep intentional slate cyber palette consistent
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkCyberColorScheme,
        typography = Typography,
        content = content
    )
}
