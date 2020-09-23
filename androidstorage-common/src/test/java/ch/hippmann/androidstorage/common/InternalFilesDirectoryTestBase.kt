package ch.hippmann.androidstorage.common

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertTrue

open class InternalFilesDirectoryTestBase {

    open fun testInternalFilesDirectory(expectedInternalDir: String) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val androidStorage = AndroidStorageImpl(context)

        val actualPath = androidStorage.getInternalFilesDirectory().path
        assertTrue("Actual internal directory is not $expectedInternalDir. It's $actualPath", actualPath.endsWith(expectedInternalDir))
    }
}