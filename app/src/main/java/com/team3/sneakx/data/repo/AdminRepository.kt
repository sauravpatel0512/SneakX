package com.team3.sneakx.data.repo

import com.team3.sneakx.data.local.dao.AdminLogDao
import com.team3.sneakx.data.local.dao.ListingDao
import com.team3.sneakx.data.local.dao.UserDao
import com.team3.sneakx.data.local.entity.AdminLogEntity
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.data.local.entity.UserEntity
import com.team3.sneakx.domain.ListingStatus
import com.team3.sneakx.domain.Role
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AdminRepository(
    private val userDao: UserDao,
    private val listingDao: ListingDao,
    private val adminLogDao: AdminLogDao
) {
    fun observeUsers(): Flow<List<UserEntity>> = userDao.observeAllUsers()

    fun observeListings(): Flow<List<ListingEntity>> = listingDao.observeAll()

    suspend fun setUserEnabled(adminId: String, user: UserEntity, enabled: Boolean) {
        if (user.role == Role.ADMIN.name && !enabled) return
        userDao.update(user.copy(enabled = enabled))
        adminLogDao.insert(
            AdminLogEntity(
                id = UUID.randomUUID().toString(),
                adminUserId = adminId,
                action = if (enabled) "ENABLE_USER" else "DISABLE_USER",
                targetType = "USER",
                targetId = user.id,
                timestampMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun removeListing(adminId: String, listing: ListingEntity) {
        listingDao.delete(listing)
        adminLogDao.insert(
            AdminLogEntity(
                id = UUID.randomUUID().toString(),
                adminUserId = adminId,
                action = "DELETE_LISTING",
                targetType = "LISTING",
                targetId = listing.id,
                timestampMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun disableListing(adminId: String, listing: ListingEntity) {
        listingDao.update(
            listing.copy(
                status = ListingStatus.DISABLED.name,
                updatedAtMillis = System.currentTimeMillis()
            )
        )
        adminLogDao.insert(
            AdminLogEntity(
                id = UUID.randomUUID().toString(),
                adminUserId = adminId,
                action = "DISABLE_LISTING",
                targetType = "LISTING",
                targetId = listing.id,
                timestampMillis = System.currentTimeMillis()
            )
        )
    }
}
