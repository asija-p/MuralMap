package com.anastasija.muralmap.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
private val Teal      = Color(0xFF1F8A84)  // primary
private val Coral     = Color(0xFFEA6F61)  // accent
private val OffWhite  = Color(0xFFF6F2EE)  // app background
private val Ink       = Color(0xFF16323A)  // text on light

private val LightColorScheme = lightColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    secondary = Coral,
    onSecondary = Color.White,
    background = OffWhite,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    outline = Color(0xFFD9D5D1) // soft gray
)


private val DarkColorScheme = darkColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    secondary = Coral,
    onSecondary = Color.White,
    background = Color(0xFF0F1719),
    onBackground = Color(0xFFE9EEF0),
    surface = Color(0xFF121A1C),
    onSurface = Color(0xFFE9EEF0),
    outline = Color(0xFF2C3A3D)
)

@Composable
fun MuralMapTheme(
    darkTheme: Boolean = false,
    // set to false to force brand colors; set true if you want Material You instead
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}