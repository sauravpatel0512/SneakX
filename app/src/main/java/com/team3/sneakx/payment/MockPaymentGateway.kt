package com.team3.sneakx.payment

import kotlinx.coroutines.delay

object MockPaymentGateway {
    /** When true, next [processPayment] returns failure (for demo / tests). */
    @Volatile
    var forceNextFailure: Boolean = false

    suspend fun processPayment(total: Double, method: String): PaymentResult {
        delay(350)
        if (forceNextFailure) {
            forceNextFailure = false
            return PaymentResult.Failure("Payment declined (mock).")
        }
        return PaymentResult.Success
    }
}

sealed class PaymentResult {
    data object Success : PaymentResult()
    data class Failure(val message: String) : PaymentResult()
}
