package com.trailmileage.diary.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ParchmentScheme = lightColorScheme(
    primary = Olive,
    onPrimary = Parchment,
    primaryContainer = OliveLight,
    onPrimaryContainer = Parchment,
    secondary = RuleLine,
    onSecondary = Ink,
    tertiary = TrailOrange,
    onTertiary = Parchment,
    background = Parchment,
    onBackground = Ink,
    surface = ParchmentLight,
    onSurface = Ink,
    surfaceVariant = ParchmentDeep,
    onSurfaceVariant = InkMuted,
    outline = RuleLine,
    error = ClayError,
    onError = Parchment
)

private val DuskScheme = darkColorScheme(
    primary = OliveLight,
    onPrimary = Dusk,
    primaryContainer = Olive,
    onPrimaryContainer = Parchment,
    secondary = RuleLine,
    onSecondary = Dusk,
    tertiary = TrailOrangeLight,
    onTertiary = Dusk,
    background = Dusk,
    onBackground = Parchment,
    surface = OliveDeep,
    onSurface = Parchment,
    surfaceVariant = OliveDeep,
    onSurfaceVariant = RuleLine,
    outline = InkMuted,
    error = ClayError,
    onError = Parchment
)

@Composable
fun AppTheme(dusk: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (dusk) DuskScheme else ParchmentScheme,
        typography = AppTypography,
        content = content
    )
}
