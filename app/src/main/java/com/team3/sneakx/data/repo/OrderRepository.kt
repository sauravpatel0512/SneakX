package com.team3.sneakx.data.repo

import com.team3.sneakx.data.local.dao.CartDao
import com.team3.sneakx.data.local.dao.ListingDao
import com.team3.sneakx.data.local.dao.OrderDao
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.data.local.entity.OrderEntity
import com.team3.sneakx.data.local.entity.OrderItemEntity
import com.team3.sneakx.domain.ListingStatus
import com.team3.sneakx.domain.OrderStatus
import com.team3.sneakx.payment.MockPaymentGateway
import com.team3.sneakx.payment.PaymentResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OrderRepository(
    private val orderDao: OrderDao,
    private val cartDao: CartDao,
    private val listingDao: ListingDao
) {
    fun observeOrders(buyerId: String): Flow<List<OrderEntity>> =
        orderDao.observeForBuyer(buyerId)

    suspend fun getOrderWithItems(orderId: String): Pair<OrderEntity, List<OrderItemEntity>>? {
        val order = orderDao.getById(orderId) ?: return null
        val items = orderDao.getItemsForOrder(orderId)
        return order to items
    }

    suspend fun checkout(
        buyerId: String,
        shippingInfo: String,
        paymentMethod: String
    ): CheckoutResult {
        val cartItems = cartDao.getItems(buyerId)
        if (cartItems.isEmpty()) {
            return CheckoutResult.Error("Cart is empty.")
        }
        val lines = mutableListOf<Triple<ListingEntity, Int, Double>>()
        for (ci in cartItems) {
            val listing = listingDao.getById(ci.listingId)
                ?: return CheckoutResult.Error("A product is no longer available.")
            if (listing.status != ListingStatus.ACTIVE.name) {
                return CheckoutResult.Error("A product is no longer available: ${listing.title}.")
            }
            lines += Triple(listing, ci.quantity, listing.price * ci.quantity)
        }
        val total = round2(lines.sumOf { it.third })
        when (val pay = MockPaymentGateway.processPayment(total, paymentMethod)) {
            is PaymentResult.Failure -> return CheckoutResult.PaymentFailed(pay.message)
            PaymentResult.Success -> { /* continue */ }
        }
        val orderId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val order = OrderEntity(
            id = orderId,
            buyerId = buyerId,
            total = total,
            paymentMethod = paymentMethod,
            shippingInfo = shippingInfo.trim(),
            status = OrderStatus.PAID.name,
            createdAtMillis = now
        )
        val orderItems = lines.map { (listing, qty, _) ->
            OrderItemEntity(
                id = UUID.randomUUID().toString(),
                orderId = orderId,
                listingId = listing.id,
                titleSnapshot = listing.title,
                unitPrice = listing.price,
                quantity = qty
            )
        }
        orderDao.insertOrderWithItems(order, orderItems)
        for ((listing, qty, _) in lines) {
            listingDao.update(
                listing.copy(
                    status = ListingStatus.SOLD.name,
                    updatedAtMillis = now
                )
            )
        }
        cartDao.clearForBuyer(buyerId)
        return CheckoutResult.Success(orderId)
    }

    private fun round2(x: Double): Double = kotlin.math.round(x * 100.0) / 100.0
}

sealed class CheckoutResult {
    data class Success(val orderId: String) : CheckoutResult()
    data class Error(val message: String) : CheckoutResult()
    data class PaymentFailed(val message: String) : CheckoutResult()
}
