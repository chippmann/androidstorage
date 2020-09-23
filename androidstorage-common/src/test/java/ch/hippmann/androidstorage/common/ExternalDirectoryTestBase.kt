package ch.hippmann.androidstorage.common

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue


open class ExternalDirectoryTestBase(
    private val mimeTypeToTest: String,
    private val expectedExternalDir: String,
    private val expectedInternalDir: String
) {

    open fun testIfExternalDirIsCorrect() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val androidStorage = AndroidStorageImpl(context)

        val actualExternalDirectory = androidStorage.getExternalDirectoryForMimeType(mimeTypeToTest).toString()
        if (actualExternalDirectory.startsWith("file://")) {
            assertTrue("Actual external directory for mimeType $mimeTypeToTest is not $expectedExternalDir. It's $actualExternalDirectory", actualExternalDirectory.endsWith(expectedExternalDir))
        } else {
            assertEquals("Actual external directory for mimeType $mimeTypeToTest is not $expectedExternalDir", expectedExternalDir, actualExternalDirectory)
        }
    }

    open fun testIfInternalDirIsCorrect() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val androidStorage = AndroidStorageImpl(context)

        val actualPath = androidStorage.getInternalDirectoryForMimeType(mimeTypeToTest).path
        assertTrue("Actual internal directory for mimeType $mimeTypeToTest is not $expectedInternalDir. It's $actualPath", actualPath.endsWith(expectedInternalDir))
    }
}