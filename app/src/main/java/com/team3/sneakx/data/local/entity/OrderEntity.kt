package com.team3.sneakx.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["buyerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("buyerId")]
)
data class OrderEntity(
    @PrimaryKey val id: String,
    val buyerId: String,
    val total: Double,
    val paymentMethod: String,
    val shippingInfo: String,
    val status: String,
    val createdAtMillis: Long
)
