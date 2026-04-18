package com.team3.sneakx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.team3.sneakx.data.session.ThemeMode
import com.team3.sneakx.ui.SneakRoot
import com.team3.sneakx.ui.theme.SneakXTheme
import androidx.compose.runtime.CompositionLocalProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as SneakXApplication
        setContent {
            val themeMode by app.container.themePreferenceStore.themeMode.collectAsState(
                initial = ThemeMode.SYSTEM
            )
            SneakXTheme(themeMode = themeMode) {
                CompositionLocalProvider(LocalAppContainer provides app.container) {
                    SneakRoot()
                }
            }
        }
    }
}
