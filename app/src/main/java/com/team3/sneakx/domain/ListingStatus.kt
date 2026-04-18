package com.team3.sneakx.domain

enum class ListingStatus {
    ACTIVE,
    SOLD,
    DISABLED;

    companion object {
        fun fromString(s: String): ListingStatus = entries.first { it.name == s }
    }
}
