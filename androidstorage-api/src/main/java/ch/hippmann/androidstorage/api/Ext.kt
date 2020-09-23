package ch.hippmann.androidstorage.api

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun Uri.fileExists(context: Context): Boolean {
    return context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        try {
            if (cursor.moveToFirst()) {
                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).isNotEmpty()
            } else {
                false
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    } ?: false
}