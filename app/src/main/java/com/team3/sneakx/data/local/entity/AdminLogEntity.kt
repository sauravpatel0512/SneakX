package com.team3.sneakx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_logs")
data class AdminLogEntity(
    @PrimaryKey val id: String,
    val adminUserId: String,
    val action: String,
    val targetType: String,
    val targetId: String,
    val timestampMillis: Long
)
