package com.team3.sneakx.data.repo

import com.team3.sneakx.data.local.entity.UserEntity
import com.team3.sneakx.data.local.dao.UserDao
import com.team3.sneakx.data.session.SessionStore
import com.team3.sneakx.domain.Role
import com.team3.sneakx.security.PasswordHasher
import java.util.UUID

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

class AuthRepository(
    private val userDao: UserDao,
    private val sessionStore: SessionStore
) {
    companion object {
        const val MAX_FAILED_ATTEMPTS = 5
        const val LOCKOUT_MILLIS = 15 * 60 * 1000L
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        role: Role
    ): RegisterResult {
        val trimmedEmail = email.trim().lowercase()
        if (name.isBlank() || trimmedEmail.isBlank() || password.isBlank()) {
            return RegisterResult.Error("All fields are required.")
        }
        if (!EMAIL_REGEX.matches(trimmedEmail)) {
            return RegisterResult.Error("Invalid email format.")
        }
        if (password.length < 6) {
            return RegisterResult.Error("Password must be at least 6 characters.")
        }
        if (role == Role.ADMIN) {
            return RegisterResult.Error("Invalid role.")
        }
        if (userDao.countByEmail(trimmedEmail) > 0) {
            return RegisterResult.Error("Email already registered.")
        }
        val salt = PasswordHasher.generateSalt()
        val hash = PasswordHasher.hash(password, salt)
        userDao.insert(
            UserEntity(
                id = UUID.randomUUID().toString(),
                name = name.trim(),
                email = trimmedEmail,
                passwordHash = hash,
                salt = salt,
                role = role.name,
                enabled = true,
                failedLoginAttempts = 0,
                lockedUntilMillis = null
            )
        )
        return RegisterResult.Success
    }

    suspend fun login(email: String, password: String): LoginResult {
        val trimmedEmail = email.trim().lowercase()
        var user = userDao.getByEmail(trimmedEmail) ?: return LoginResult.InvalidCredentials
        val now = System.currentTimeMillis()
        if (!user.enabled) {
            return LoginResult.AccountDisabled
        }
        val until = user.lockedUntilMillis
        if (until != null && now >= until) {
            user = user.copy(lockedUntilMillis = null, failedLoginAttempts = 0)
            userDao.update(user)
        }
        user.lockedUntilMillis?.let { activeUntil ->
            if (now < activeUntil) {
                return LoginResult.Locked(activeUntil)
            }
        }
        val ok = PasswordHasher.verify(password, user.salt, user.passwordHash)
        if (!ok) {
            val attempts = user.failedLoginAttempts + 1
            val lockedUntil = if (attempts >= MAX_FAILED_ATTEMPTS) now + LOCKOUT_MILLIS else null
            userDao.update(
                user.copy(
                    failedLoginAttempts = attempts,
                    lockedUntilMillis = lockedUntil
                )
            )
            return if (attempts >= MAX_FAILED_ATTEMPTS && lockedUntil != null) {
                LoginResult.Locked(lockedUntil)
            } else {
                LoginResult.InvalidCredentials
            }
        }
        userDao.update(
            user.copy(failedLoginAttempts = 0, lockedUntilMillis = null)
        )
        sessionStore.setSession(
            userId = user.id,
            email = user.email,
            role = Role.fromString(user.role)
        )
        return LoginResult.Success
    }

    suspend fun logout() {
        sessionStore.clear()
    }
}

sealed class RegisterResult {
    data object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}

sealed class LoginResult {
    data object Success : LoginResult()
    data object InvalidCredentials : LoginResult()
    data object AccountDisabled : LoginResult()
    data class Locked(val untilMillis: Long) : LoginResult()
}
