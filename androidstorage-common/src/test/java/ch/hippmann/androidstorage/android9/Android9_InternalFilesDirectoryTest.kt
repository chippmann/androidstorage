package ch.hippmann.androidstorage.android9

import ch.hippmann.androidstorage.common.InternalFilesDirectoryTestBase
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class Android9_InternalFilesDirectoryTest : InternalFilesDirectoryTestBase() {

    @Test
    fun testInternalFilesDirectory() {
        super.testInternalFilesDirectory("/files")
    }
}