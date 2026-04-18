package com.team3.sneakx.data.repo

import com.team3.sneakx.data.local.dao.CartDao
import com.team3.sneakx.data.local.dao.ListingDao
import com.team3.sneakx.data.local.entity.CartItemEntity
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.domain.ListingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

data class CartLineUi(
    val listing: ListingEntity,
    val quantity: Int,
    val lineTotal: Double
)

class CartRepository(
    private val cartDao: CartDao,
    private val listingDao: ListingDao
) {
    fun observeCartLines(buyerId: String): Flow<List<CartLineUi>> {
        return cartDao.observeItems(buyerId).mapLatest { items ->
            items.mapNotNull { ci ->
                val listing = listingDao.getById(ci.listingId) ?: return@mapNotNull null
                if (listing.status != ListingStatus.ACTIVE.name) return@mapNotNull null
                CartLineUi(
                    listing = listing,
                    quantity = ci.quantity,
                    lineTotal = round2(listing.price * ci.quantity)
                )
            }
        }
    }

    suspend fun addToCart(buyerId: String, listingId: String, quantity: Int = 1) {
        val listing = listingDao.getById(listingId) ?: return
        if (listing.status != ListingStatus.ACTIVE.name) return
        val existing = cartDao.getItems(buyerId).find { it.listingId == listingId }
        val newQty = (existing?.quantity ?: 0) + quantity
        cartDao.upsert(CartItemEntity(buyerId = buyerId, listingId = listingId, quantity = newQty))
    }

    suspend fun setQuantity(buyerId: String, listingId: String, quantity: Int) {
        if (quantity <= 0) {
            cartDao.remove(buyerId, listingId)
        } else {
            cartDao.upsert(CartItemEntity(buyerId = buyerId, listingId = listingId, quantity = quantity))
        }
    }

    suspend fun removeLine(buyerId: String, listingId: String) {
        cartDao.remove(buyerId, listingId)
    }

    suspend fun clearCart(buyerId: String) {
        cartDao.clearForBuyer(buyerId)
    }

    suspend fun cartSubtotal(buyerId: String): Double {
        val items = cartDao.getItems(buyerId)
        var sum = 0.0
        for (ci in items) {
            val l = listingDao.getById(ci.listingId) ?: continue
            if (l.status == ListingStatus.ACTIVE.name) {
                sum += l.price * ci.quantity
            }
        }
        return round2(sum)
    }

    private fun round2(x: Double): Double =
        kotlin.math.round(x * 100.0) / 100.0
}
