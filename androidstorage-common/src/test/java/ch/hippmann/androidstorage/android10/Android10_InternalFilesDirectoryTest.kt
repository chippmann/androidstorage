package ch.hippmann.androidstorage.android10

import ch.hippmann.androidstorage.common.InternalFilesDirectoryTestBase
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class Android10_InternalFilesDirectoryTest : InternalFilesDirectoryTestBase() {

    @Test
    fun testInternalFilesDirectory() {
        super.testInternalFilesDirectory("/files")
    }
}