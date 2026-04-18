package com.team3.sneakx.data.repo

import com.team3.sneakx.data.local.dao.ListingDao
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.domain.ListingSort
import com.team3.sneakx.domain.ListingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import java.util.UUID

class ListingRepository(
    private val listingDao: ListingDao
) {
    fun observeMarketplaceListings(
        keyword: String,
        category: String?,
        minPrice: Double?,
        maxPrice: Double?,
        sort: ListingSort
    ): Flow<List<ListingEntity>> {
        return listingDao.observeByStatus(ListingStatus.ACTIVE.name).map { list ->
            list.asSequence()
                .filter { le ->
                    val kw = keyword.trim().lowercase(Locale.getDefault())
                    if (kw.isEmpty()) true
                    else le.title.lowercase(Locale.getDefault()).contains(kw) ||
                        le.description.lowercase(Locale.getDefault()).contains(kw)
                }
                .filter { le ->
                    category.isNullOrBlank() || le.category == category
                }
                .filter { le ->
                    val min = minPrice ?: Double.NEGATIVE_INFINITY
                    val max = maxPrice ?: Double.POSITIVE_INFINITY
                    le.price in min..max
                }
                .sortedWith(sortComparator(sort))
                .toList()
        }
    }

    private fun sortComparator(sort: ListingSort): Comparator<ListingEntity> =
        when (sort) {
            ListingSort.NEWEST -> compareByDescending { it.createdAtMillis }
            ListingSort.PRICE_LOW -> compareBy { it.price }
            ListingSort.PRICE_HIGH -> compareByDescending { it.price }
            ListingSort.TITLE_AZ -> compareBy { it.title.lowercase(Locale.getDefault()) }
        }

    fun observeSellerListings(sellerId: String): Flow<List<ListingEntity>> =
        listingDao.observeBySeller(sellerId)

    suspend fun getListing(id: String): ListingEntity? = listingDao.getById(id)

    suspend fun createListing(
        sellerId: String,
        title: String,
        description: String,
        category: String,
        price: Double,
        condition: String,
        photosJson: String
    ): String {
        val id = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        listingDao.insert(
            ListingEntity(
                id = id,
                sellerId = sellerId,
                title = title.trim(),
                description = description.trim(),
                category = category.trim(),
                price = price,
                condition = condition.trim(),
                photosJson = photosJson,
                status = ListingStatus.ACTIVE.name,
                createdAtMillis = now,
                updatedAtMillis = now
            )
        )
        return id
    }

    suspend fun updateListing(
        existing: ListingEntity,
        title: String,
        description: String,
        category: String,
        price: Double,
        condition: String,
        photosJson: String
    ) {
        val now = System.currentTimeMillis()
        listingDao.update(
            existing.copy(
                title = title.trim(),
                description = description.trim(),
                category = category.trim(),
                price = price,
                condition = condition.trim(),
                photosJson = photosJson,
                updatedAtMillis = now
            )
        )
    }

    suspend fun deleteListing(listing: ListingEntity) {
        listingDao.delete(listing)
    }

    suspend fun setListingStatus(listing: ListingEntity, status: ListingStatus) {
        listingDao.update(
            listing.copy(
                status = status.name,
                updatedAtMillis = System.currentTimeMillis()
            )
        )
    }

    fun observeAllListingsAdmin(): Flow<List<ListingEntity>> = listingDao.observeAll()
}
