package ch.hippmann.androidstorage.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import ch.hippmann.androidstorage.api.AndroidStorage
import ch.hippmann.androidstorage.api.builder.AndroidStorageWriteBuilder
import ch.hippmann.androidstorage.common.builder.AndroidStorageWriteBuilderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream

class AndroidStorageImpl(
    override val context: Context
) : AndroidStorage {

    override fun getExternalDirectoryForMimeType(mimeType: String): Uri {
        val mimeTypeParts = mimeType.split("/")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (mimeTypeParts.isEmpty()) {
                return MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            }
            when (mimeTypeParts[0]) {
                "image" -> MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                "video" -> MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                "audio" -> MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                else -> MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            }
        } else {
            if (mimeTypeParts.isEmpty()) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).also { it.mkdirs() }.toUri()
            }
            when (mimeTypeParts[0]) {
                "image" -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).also { it.mkdirs() }.toUri()
                "video" -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).also { it.mkdirs() }.toUri()
                "audio" -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).also { it.mkdirs() }.toUri()
                else -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).also { it.mkdirs() }.toUri()
            }
        }
    }

    override fun getInternalDirectoryForMimeType(mimeType: String): File {
        val mimeTypeParts = mimeType.split("/")
        if (mimeTypeParts.isEmpty()) {
            return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.also { it.mkdirs() } ?: throw IllegalStateException("Could not find internal file directory")
        }
        return when (mimeTypeParts[0]) {
            "image" -> context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also { it.mkdirs() } ?: throw IllegalStateException("Could not find internal file directory")
            "video" -> context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.also { it.mkdirs() } ?: throw IllegalStateException("Could not find internal file directory")
            "audio" -> context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.also { it.mkdirs() } ?: throw IllegalStateException("Could not find internal file directory")
            else -> context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.also { it.mkdirs() } ?: throw IllegalStateException("Could not find internal file directory")
        }
    }

    override fun getInternalFilesDirectory(): File {
        return context.filesDir
    }

    override suspend fun saveExternal(name: String, destinationDir: Uri, writeBuilder: suspend AndroidStorageWriteBuilder.() -> Unit) {
        val storageBuilder = AndroidStorageWriteBuilderImpl(context)
        writeBuilder(storageBuilder)
        storageBuilder.build(destinationDir, name)
    }

    override suspend fun saveInternal(name: String, destinationDir: File, writeAction: suspend (OutputStream) -> Unit): Uri {
        val file = File(destinationDir, name)
        file.mkdirs()
        file.outputStream().use {
            writeAction(it)
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    override suspend fun copyExternalFileToInternalDirectory(externalFile: Uri, destinationDir: File): Uri? {
        return context.contentResolver.query(externalFile, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                val name = cursor.getString(nameIndex)
                withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(externalFile)?.use { inputStream ->
                        return@withContext saveInternal(name, destinationDir) {
                            inputStream.copyTo(it)
                        }
                    }
                }
            } else {
                null
            }
        }
    }

    override fun getFileSize(uri: Uri): Long {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                cursor.getLong(sizeIndex)
            } else {
                -1L
            }
        } ?: -1L
    }

    override fun getUriForInternalFile(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    override fun getActionViewIntentForInternalFile(uri: Uri): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            setDataAndType(uri, context.contentResolver.getType(uri))
        }
    }

    override fun getActionViewAndModifyIntentForInternalFile(uri: Uri): Intent {
        return getActionViewIntentForInternalFile(uri).apply {
            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }
    }
}