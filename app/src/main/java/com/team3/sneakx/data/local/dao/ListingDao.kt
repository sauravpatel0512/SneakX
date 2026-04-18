package com.team3.sneakx.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team3.sneakx.data.local.entity.ListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listing: ListingEntity)

    @Update
    suspend fun update(listing: ListingEntity)

    @Delete
    suspend fun delete(listing: ListingEntity)

    @Query("SELECT * FROM listings WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ListingEntity?

    @Query("SELECT * FROM listings WHERE sellerId = :sellerId ORDER BY updatedAtMillis DESC")
    fun observeBySeller(sellerId: String): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings ORDER BY createdAtMillis DESC")
    fun observeAll(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE status = :status ORDER BY createdAtMillis DESC")
    fun observeByStatus(status: String): Flow<List<ListingEntity>>
}
