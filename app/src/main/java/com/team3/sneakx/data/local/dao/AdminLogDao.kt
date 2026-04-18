package com.team3.sneakx.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team3.sneakx.data.local.entity.AdminLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: AdminLogEntity)

    @Query("SELECT * FROM admin_logs ORDER BY timestampMillis DESC LIMIT 200")
    fun observeRecent(): Flow<List<AdminLogEntity>>
}
