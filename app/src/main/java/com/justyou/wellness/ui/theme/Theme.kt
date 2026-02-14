package com.justyou.wellness.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val JustYouColorScheme = lightColorScheme(
    primary = PastelBlue,
    secondary = PastelPurple,
    tertiary = PastelGreen,
    background = BackgroundPrimary,
    surface = CardBackground,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onTertiary = TextWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = CardBackgroundSecondary,
    outline = TextMuted
)

@Composable
fun JustYouTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = JustYouColorScheme,
        typography = JustYouTypography,
        content = content
    )
}
