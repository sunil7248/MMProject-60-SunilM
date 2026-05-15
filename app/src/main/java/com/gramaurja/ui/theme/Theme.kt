package com.gramaurja.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    secondary = AccentYellow,
    tertiary = GreenOn,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceWarm,
    onPrimary = TextLight,
    onSecondary = TextDark,
    onTertiary = TextLight,
    onBackground = TextDark,
    onSurface = TextDark,
    onSurfaceVariant = DeepTeal,
    error = RedOff
)

private val DarkColors = darkColorScheme(
    primary = DeepTeal,
    secondary = AccentYellow,
    tertiary = GreenOn,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = PrimaryBlueDark,
    onPrimary = TextLight,
    onSecondary = TextDark,
    onTertiary = TextLight,
    onBackground = TextLight,
    onSurface = TextLight,
    onSurfaceVariant = Color(0xFFD3DCCB),
    error = RedOff
)

@Composable
fun GramaUrjaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = GramaTypography,
        content = content
    )
}
