package ch.hippmann.androidstorage.common.builder

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import ch.hippmann.androidstorage.api.builder.AndroidStorageWriteBuilder
import ch.hippmann.androidstorage.api.dsl.AndroidStorageWriteDsl
import ch.hippmann.androidstorage.api.exception.PermissionRequiredException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import java.io.File
import java.io.OutputStream
import kotlin.coroutines.coroutineContext

@AndroidStorageWriteDsl
class AndroidStorageWriteBuilderImpl(private val context: Context, private val isInternal: Boolean = true) : AndroidStorageWriteBuilder(context, isInternal) {
    private val contentValues = ContentValues().apply {
        val date = DateTime.now().millis
        put(MediaStore.MediaColumns.DATE_ADDED, date)
        put(MediaStore.MediaColumns.DATE_MODIFIED, date)
    }

    private var onPermissionRequired: (() -> Unit)? = null
    private var failureAction: (Throwable) -> Unit = {}
    private var successAction: (Uri) -> Unit = {}
    private var write: suspend (OutputStream) -> Unit = {}

    override fun contentValues(values: ContentValues.() -> Unit) {
        values(contentValues)
    }

    override fun onPermissionRequired(onPermissionRequiredAction: () -> Unit) {
        onPermissionRequired = onPermissionRequiredAction
    }

    override fun onFailure(onFailureAction: (Throwable) -> Unit) {
        failureAction = onFailureAction
    }

    override fun onSuccess(onSuccessAction: (Uri) -> Unit) {
        successAction = onSuccessAction
    }

    override fun write(writeAction: suspend (OutputStream) -> Unit) {
        write = writeAction
    }

    override suspend fun build(destinationDir: Uri, name: String) {
        if (!isInternal && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            onPermissionRequired?.let { it() } ?: throw PermissionRequiredException("Permission ${Manifest.permission.WRITE_EXTERNAL_STORAGE} is required in order to write to external storage")
            return
        }

        val startingContext = coroutineContext
        withContext(Dispatchers.IO) rootContext@{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)

                val uri = context.contentResolver.insert(destinationDir, contentValues)
                if (uri == null) {
                    withContext(startingContext) {
                        failureAction(NullPointerException("ContentResolver insert returned null"))
                    }
                    return@rootContext
                }

                @Suppress("BlockingMethodInNonBlockingContext") //we are on the I/O thread in this context, so it's supposed to block
                context.contentResolver.openOutputStream(uri, "w").use { outputStream ->
                    if (outputStream == null) {
                        withContext(startingContext) {
                            failureAction(NullPointerException("OutputStream null"))
                        }
                        return@rootContext
                    }
                    withContext(startingContext) {
                        write(outputStream)
                    }
                }

                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                context.contentResolver.update(uri, contentValues, null, null)
                withContext(startingContext) {
                    successAction(uri)
                }
            } else {
                val outputFile = File(destinationDir.toFile(), name)
                if (!outputFile.exists() && !outputFile.createNewFile()) {
                    withContext(startingContext) {
                        failureAction(IllegalStateException("File does not exist and cannot be created: $outputFile"))
                    }
                    return@rootContext
                }

                outputFile.outputStream().use { outputStream ->
                    write(outputStream)
                }

                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = outputFile.toUri()
                context.sendBroadcast(mediaScanIntent)

                withContext(startingContext) {
                    successAction(outputFile.toUri())
                }
            }
        }
    }
}