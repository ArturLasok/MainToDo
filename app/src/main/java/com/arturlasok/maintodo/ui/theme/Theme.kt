package com.arturlasok.maintodo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


private val DarkColorPalette = darkColors(

    primary = primaryDark,
    onPrimary = onPrimaryDark,

    primaryVariant = primaryVariantDark,

    secondary = secondaryDark,
    onSecondary = onSecondaryDark,

    background = backgroundDark,
    onBackground = onBackgroundDark,

    surface = surfaceDark,
    onSurface = onSurfaceDark,
)

private val LightColorPalette = lightColors(
    primary = primary,
    onPrimary = onPrimary,

    primaryVariant = primaryVariant,

    secondary = secondary,
    onSecondary = onSecondary,

    background = background,
    onBackground = onBackground,

    surface = surface,
    onSurface = onSurface,
)

@Composable
fun MainToDoTheme(darkTheme: Int = 0, content: @Composable () -> Unit) {
    val colors =
    when(darkTheme) {
        0  -> if(isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
        1  -> LightColorPalette
        2  -> DarkColorPalette
        else -> { LightColorPalette }
    }


        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )


}