package com.team3.sneakx.domain

enum class Role {
    BUYER,
    SELLER,
    ADMIN;

    companion object {
        fun fromString(s: String): Role = entries.first { it.name == s }
    }
}
