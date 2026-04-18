package com.team3.sneakx.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team3.sneakx.data.local.dao.AdminLogDao
import com.team3.sneakx.data.local.dao.CartDao
import com.team3.sneakx.data.local.dao.ListingDao
import com.team3.sneakx.data.local.dao.OrderDao
import com.team3.sneakx.data.local.dao.UserDao
import com.team3.sneakx.data.local.entity.AdminLogEntity
import com.team3.sneakx.data.local.entity.CartItemEntity
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.data.local.entity.OrderEntity
import com.team3.sneakx.data.local.entity.OrderItemEntity
import com.team3.sneakx.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ListingEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        AdminLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun listingDao(): ListingDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun adminLogDao(): AdminLogDao
}
