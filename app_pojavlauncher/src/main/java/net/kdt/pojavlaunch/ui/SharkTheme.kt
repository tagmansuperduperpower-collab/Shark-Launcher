package net.kdt.pojavlaunch.ui

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SharkColors = darkColorScheme(
    primary = Color(0xFF63D8FF),
    onPrimary = Color(0xFF003544),
    primaryContainer = Color(0xFF14556A),
    onPrimaryContainer = Color(0xFFB8EEFF),
    secondary = Color(0xFF9FC9DB),
    onSecondary = Color(0xFF063543),
    secondaryContainer = Color(0xFF274A59),
    onSecondaryContainer = Color(0xFFC5EAF8),
    tertiary = Color(0xFFB5C7FF),
    background = Color(0xFF07111F),
    onBackground = Color(0xFFE8F2F7),
    surface = Color(0xFF0A1726),
    onSurface = Color(0xFFE8F2F7),
    surfaceVariant = Color(0xFF1B2B3B),
    onSurfaceVariant = Color(0xFFB5C5D1),
    outline = Color(0xFF526B7B),
    outlineVariant = Color(0xFF2A4050)
)

private val SharkTypography = Typography(
    headlineLarge = Typography().headlineLarge.copy(fontSize = 30.sp, lineHeight = 36.sp),
    headlineMedium = Typography().headlineMedium.copy(fontSize = 24.sp, lineHeight = 30.sp),
    titleLarge = Typography().titleLarge.copy(fontSize = 20.sp),
    titleMedium = Typography().titleMedium.copy(fontSize = 16.sp),
    bodyLarge = Typography().bodyLarge.copy(fontSize = 16.sp, lineHeight = 23.sp),
    bodyMedium = Typography().bodyMedium.copy(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = Typography().labelLarge.copy(fontSize = 13.sp, letterSpacing = 0.5.sp),
    labelSmall = Typography().labelSmall.copy(fontSize = 11.sp, letterSpacing = 0.6.sp)
)

private val SharkShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
)

object SharkMotion {
    val standard: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    val emphasized: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
}

@Composable
fun SharkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SharkColors,
        typography = SharkTypography,
        shapes = SharkShapes,
        content = content
    )
}
