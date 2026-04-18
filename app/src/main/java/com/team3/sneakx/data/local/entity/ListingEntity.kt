package com.team3.sneakx.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "listings",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["sellerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sellerId"), Index("status")]
)
data class ListingEntity(
    @PrimaryKey val id: String,
    val sellerId: String,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val condition: String,
    val photosJson: String,
    val status: String,
    val createdAtMillis: Long,
    val updatedAtMillis: Long
)
