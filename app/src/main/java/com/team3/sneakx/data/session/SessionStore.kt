package com.team3.sneakx.data.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.team3.sneakx.domain.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

data class Session(
    val userId: String?,
    val email: String?,
    val role: Role?
)

class SessionStore(private val context: Context) {
    private val keyUserId = stringPreferencesKey("user_id")
    private val keyEmail = stringPreferencesKey("email")
    private val keyRole = stringPreferencesKey("role")

    val session: Flow<Session> = context.sessionDataStore.data.map { prefs ->
        val id = prefs[keyUserId]
        val email = prefs[keyEmail]
        val roleStr = prefs[keyRole]
        Session(
            userId = id,
            email = email,
            role = roleStr?.let { runCatching { Role.fromString(it) }.getOrNull() }
        )
    }

    suspend fun setSession(userId: String, email: String, role: Role) {
        context.sessionDataStore.edit { prefs ->
            prefs[keyUserId] = userId
            prefs[keyEmail] = email
            prefs[keyRole] = role.name
        }
    }

    suspend fun clear() {
        context.sessionDataStore.edit { it.clear() }
    }
}
