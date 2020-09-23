package ch.hippmann.androidstorage.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import ch.hippmann.androidstorage.api.builder.AndroidStorageWriteBuilder
import java.io.File
import java.io.OutputStream

interface AndroidStorage {
    val context: Context
    fun getExternalDirectoryForMimeType(mimeType: String): Uri
    fun getInternalDirectoryForMimeType(mimeType: String): File
    fun getInternalFilesDirectory(): File
    suspend fun saveExternal(name: String, destinationDir: Uri, writeBuilder: suspend AndroidStorageWriteBuilder.() -> Unit)
    suspend fun saveInternal(name: String, destinationDir: File, writeAction: suspend (OutputStream) -> Unit): Uri
    suspend fun copyExternalFileToInternalDirectory(externalFile: Uri, destinationDir: File = getInternalDirectoryForMimeType(context.contentResolver.getType(externalFile) ?: "*/*")): Uri?
    fun getFileSize(uri: Uri): Long

    @Throws(IllegalArgumentException::class)
    fun getUriForInternalFile(file: File): Uri
    fun getActionViewIntentForInternalFile(uri: Uri): Intent
    fun getActionViewAndModifyIntentForInternalFile(uri: Uri): Intent
}