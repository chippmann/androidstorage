package ch.hippmann.androidstorage.android8_1

import ch.hippmann.androidstorage.common.ExternalDirectoryTestBase
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [27])
class Android8_1_ExternalDirectoryTest(
    mimeTypeToTest: String,
    expectedExternalDirUri: String,
    expectedInternalDirEnd: String
) : ExternalDirectoryTestBase(mimeTypeToTest, expectedExternalDirUri, expectedInternalDirEnd) {

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "External dir for mimeType \"{0}\" should be \"{1}\" and internal dir should be \"{2}\"")
        fun params() = listOf(
            arrayOf("image/jpeg", "/external-files/Pictures", "/files/Pictures"),
            arrayOf("image/png", "/external-files/Pictures", "/files/Pictures"),
            arrayOf("video/mpeg", "/external-files/Movies", "/files/Movies"),
            arrayOf("audio/mpeg", "/external-files/Music", "/files/Music"),
            arrayOf("application/pdf", "/external-files/Documents", "/files/Documents"),
            arrayOf("*/*", "/external-files/Documents", "/files/Documents")
        )
    }

    @Test
    override fun testIfExternalDirIsCorrect() {
        super.testIfExternalDirIsCorrect()
    }

    @Test
    override fun testIfInternalDirIsCorrect() {
        super.testIfInternalDirIsCorrect()
    }
}