package ch.hippmann.androidstorage.android8_1

import ch.hippmann.androidstorage.common.InternalFilesDirectoryTestBase
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [27])
class Android8_1_InternalFilesDirectoryTest : InternalFilesDirectoryTestBase() {

    @Test
    fun testInternalFilesDirectory() {
        super.testInternalFilesDirectory("/files")
    }
}