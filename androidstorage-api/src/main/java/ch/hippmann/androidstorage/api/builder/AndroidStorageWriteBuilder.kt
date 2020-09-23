package ch.hippmann.androidstorage.api.builder

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import ch.hippmann.androidstorage.api.dsl.AndroidStorageWriteDsl
import java.io.OutputStream

@AndroidStorageWriteDsl
abstract class AndroidStorageWriteBuilder(private val context: Context, private val isInternal: Boolean = true) {
    abstract fun contentValues(values: ContentValues.() -> Unit)
    abstract fun onPermissionRequired(onPermissionRequiredAction: () -> Unit)
    abstract fun onFailure(onFailureAction: (Throwable) -> Unit)
    abstract fun onSuccess(onSuccessAction: (Uri) -> Unit)
    abstract fun write(writeAction: suspend (OutputStream) -> Unit)
    abstract suspend fun build(destinationDir: Uri, name: String)
}