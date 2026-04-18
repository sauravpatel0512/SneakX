package com.team3.sneakx.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team3.sneakx.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE buyerId = :buyerId AND listingId = :listingId")
    suspend fun remove(buyerId: String, listingId: String)

    @Query("DELETE FROM cart_items WHERE buyerId = :buyerId")
    suspend fun clearForBuyer(buyerId: String)

    @Query("SELECT * FROM cart_items WHERE buyerId = :buyerId")
    fun observeItems(buyerId: String): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE buyerId = :buyerId")
    suspend fun getItems(buyerId: String): List<CartItemEntity>
}
