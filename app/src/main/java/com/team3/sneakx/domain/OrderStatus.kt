package com.team3.sneakx.domain

enum class OrderStatus {
    PENDING,
    PAID,
    CANCELLED;

    companion object {
        fun fromString(s: String): OrderStatus = entries.first { it.name == s }
    }
}
