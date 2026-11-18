package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val AstroDarkColorScheme = darkColorScheme(
    primary = CosmicPurple,
    onPrimary = StarWhite,
    primaryContainer = ElevatedCosmic,
    onPrimaryContainer = SoftLavender,
    secondary = NeonCyan,
    onSecondary = DeepCosmos,
    secondaryContainer = DeepCyanContainer,
    onSecondaryContainer = NeonCyan,
    tertiary = SolarYellow,
    onTertiary = DeepCosmos,
    tertiaryContainer = SolarContainer,
    onTertiaryContainer = SolarYellow,
    background = DeepCosmos,
    onBackground = StarWhite,
    surface = NebulaSurface,
    onSurface = StarWhite,
    surfaceVariant = StarlightCard,
    onSurfaceVariant = MoonSilver,
    outline = OrbitBorder,
    outlineVariant = MidnightOrbit,
    error = ErrorCoral,
    onError = DeepCosmos,
    errorContainer = ErrorContainerDark,
    onErrorContainer = ErrorCoral
)

val AstroShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun AstroMateTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AstroDarkColorScheme,
        typography = AstroTypography,
        shapes = AstroShapes,
        content = content
    )
}
