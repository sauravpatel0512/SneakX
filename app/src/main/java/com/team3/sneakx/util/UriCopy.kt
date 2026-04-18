package com.team3.sneakx.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun copyContentUriToAppFiles(context: Context, uri: Uri): String {
    val ext = context.contentResolver.getType(uri)?.substringAfterLast("/")?.takeIf { it.length <= 5 } ?: "jpg"
    val file = File(context.filesDir, "img_${UUID.randomUUID()}.$ext")
    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(file).use { output -> input.copyTo(output) }
    } ?: throw IllegalStateException("Cannot open $uri")
    return Uri.fromFile(file).toString()
}
