package ch.hippmann.androidstorage.android11

//TODO: enable with next robolectric release that supports Android R (Android 11, Api Level 30)
//@RunWith(ParameterizedRobolectricTestRunner::class)
//@Config(sdk = [30])
//class Android11_ExternalDirectoryTest(
//    mimeTypeToTest: String,
//    expectedExternalDirUri: String,
//    expectedInternalDirEnd: String
//) : ExternalDirectoryTestBase(mimeTypeToTest, expectedExternalDirUri, expectedInternalDirEnd) {
//
//    companion object {
//        @JvmStatic
//        @ParameterizedRobolectricTestRunner.Parameters(name = "External dir for mimeType \"{0}\" should be \"{1}\" and internal dir should be \"{2}\"")
//        fun params() = listOf(
//            arrayOf("image/jpeg", "content://media/external_primary/images/media", "/external-files/Pictures"),
//            arrayOf("image/png", "content://media/external_primary/images/media", "/external-files/Pictures"),
//            arrayOf("video/mpeg", "content://media/external_primary/video/media", "/external-files/Movies"),
//            arrayOf("audio/mpeg", "content://media/external_primary/audio/media", "/external-files/Music"),
//            arrayOf("application/pdf", "content://media/external_primary/file", "/external-files/Documents"),
//            arrayOf("*/*", "content://media/external_primary/file", "/external-files/Documents")
//        )
//    }
//
//    @Test
//    override fun testIfExternalDirIsCorrect() {
//        super.testIfExternalDirIsCorrect()
//    }
//
//    @Test
//    override fun testIfInternalDirIsCorrect() {
//        super.testIfInternalDirIsCorrect()
//    }
//}