package com.team3.sneakx.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val passwordHash: String,
    val salt: String,
    val role: String,
    val avatarUrl: String? = null,
    val enabled: Boolean = true,
    val failedLoginAttempts: Int = 0,
    val lockedUntilMillis: Long? = null
)
