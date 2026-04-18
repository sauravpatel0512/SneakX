package com.team3.sneakx.data.repo

import com.team3.sneakx.data.local.dao.UserDao
import com.team3.sneakx.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class ProfileRepository(
    private val userDao: UserDao
) {
    fun observeUser(userId: String): Flow<UserEntity?> = userDao.observeUser(userId)

    suspend fun updateProfile(user: UserEntity, newName: String, newEmail: String): ProfileUpdateResult {
        val name = newName.trim()
        val email = newEmail.trim().lowercase()
        if (name.isBlank() || email.isBlank()) {
            return ProfileUpdateResult.Error("Name and email are required.")
        }
        if (!EMAIL_REGEX.matches(email)) {
            return ProfileUpdateResult.Error("Invalid email format.")
        }
        val other = userDao.getByEmail(email)
        if (other != null && other.id != user.id) {
            return ProfileUpdateResult.Error("Email already in use.")
        }
        userDao.update(user.copy(name = name, email = email))
        return ProfileUpdateResult.Success
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    }
}

sealed class ProfileUpdateResult {
    data object Success : ProfileUpdateResult()
    data class Error(val message: String) : ProfileUpdateResult()
}
