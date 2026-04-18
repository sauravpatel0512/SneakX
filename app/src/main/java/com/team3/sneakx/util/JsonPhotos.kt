package com.team3.sneakx.util

import org.json.JSONArray

fun photosFromJson(json: String): List<String> {
    if (json.isBlank()) return emptyList()
    return try {
        val arr = JSONArray(json)
        (0 until arr.length()).map { arr.getString(it) }
    } catch (_: Exception) {
        emptyList()
    }
}

fun photosToJson(uris: List<String>): String {
    val arr = JSONArray()
    uris.forEach { arr.put(it) }
    return arr.toString()
}
