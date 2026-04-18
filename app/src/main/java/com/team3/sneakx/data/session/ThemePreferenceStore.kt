package com.team3.sneakx.data.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

class ThemePreferenceStore(context: Context) {
    private val appContext = context.applicationContext
    private val keyDark = booleanPreferencesKey("use_dark")
    private val keyHasOverride = booleanPreferencesKey("has_override")

    val themeMode: Flow<ThemeMode> = appContext.themeDataStore.data.map { prefs ->
        when {
            prefs[keyHasOverride] != true -> ThemeMode.SYSTEM
            prefs[keyDark] == true -> ThemeMode.DARK
            else -> ThemeMode.LIGHT
        }
    }

    suspend fun setDarkOverride(useDark: Boolean?) {
        appContext.themeDataStore.edit { p ->
            if (useDark == null) {
                p[keyHasOverride] = false
            } else {
                p[keyHasOverride] = true
                p[keyDark] = useDark
            }
        }
    }
}
