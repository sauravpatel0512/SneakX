package com.team3.sneakx.ui.theme

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import com.team3.sneakx.data.session.ThemeMode

/**
 * Root theme for SneakX: [SneakLightColorScheme] / [SneakDarkColorScheme], [Typography], [SneakShapes].
 * Extended colors: [LocalSneakExtendedColors]. Use [MaterialTheme] + [SneakSpacing] for layout rhythm.
 *
 * By default **static brand colors** are used on all API levels so the marketplace looks consistent.
 * Set [useDynamicColor] to `true` on API 31+ to use the device’s Material You dynamic palette (wallpaper-based);
 * use sparingly when you want system integration over fixed branding.
 */
@Composable
fun SneakXTheme(
    themeMode: ThemeMode,
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }
    val context = LocalContext.current
    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        dark -> SneakDarkColorScheme
        else -> SneakLightColorScheme
    }
    CompositionLocalProvider(
        LocalSneakExtendedColors provides SneakExtendedColors(success = SneakSuccess),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = SneakShapes,
            content = content,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun SneakXThemeLightPreview() {
    SneakXTheme(themeMode = ThemeMode.LIGHT) {
        ThemePreviewSample()
    }
}

@Preview(name = "Dark", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SneakXThemeDarkPreview() {
    SneakXTheme(themeMode = ThemeMode.DARK) {
        ThemePreviewSample()
    }
}

@Composable
private fun ThemePreviewSample() {
    Column(Modifier.padding(SneakSpacing.lg)) {
        Text("SneakX", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Marketplace preview",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Card(
            modifier = Modifier.padding(top = SneakSpacing.md),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            Text(
                "Product card",
                modifier = Modifier.padding(SneakSpacing.cardPadding),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
