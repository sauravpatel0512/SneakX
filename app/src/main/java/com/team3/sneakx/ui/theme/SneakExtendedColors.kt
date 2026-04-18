package com.team3.sneakx.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Colors not covered by [androidx.compose.material3.ColorScheme] (e.g. success deltas).
 */
data class SneakExtendedColors(
    val success: Color = SneakSuccess,
)

val LocalSneakExtendedColors = staticCompositionLocalOf { SneakExtendedColors() }

object SneakThemeExtras {
    val success: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSneakExtendedColors.current.success
}
