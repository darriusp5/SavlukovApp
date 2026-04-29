package com.savlukov.app.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SavlukovPrimaryDark,
    onPrimary = SavlukovOnPrimaryDark,
    secondary = Champagne,
    onSecondary = Onyx,
    tertiary = Champagne,
    onTertiary = Onyx,
    background = SavlukovBackgroundDark,
    surface = SavlukovSurfaceDark,
    onSurface = SavlukovOnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = SavlukovPrimary,
    onPrimary = SavlukovOnPrimary,
    secondary = SavlukovSecondary,
    onSecondary = SavlukovOnSecondary,
    background = SavlukovBackground,
    surface = SavlukovSurface,
    onSurface = SavlukovOnSurface
)

@Composable
fun SavlukovTheme(
    darkTheme: Boolean = true, // Always use dark theme for black-gold aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            @Suppress("DEPRECATION")
            try {
                val window = (view.context as Activity).window
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            } catch (e: Exception) {
                // Ignore errors in status bar configuration
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
