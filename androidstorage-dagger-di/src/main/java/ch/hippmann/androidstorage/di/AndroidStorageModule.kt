package ch.hippmann.androidstorage.di

import android.content.Context
import ch.hippmann.androidstorage.api.AndroidStorage
import ch.hippmann.androidstorage.common.AndroidStorageImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object AndroidStorageModule {

    @JvmStatic
    @Provides
    @Reusable
    fun provideAndroidStorage(context: Context): AndroidStorage = AndroidStorageImpl(context)
}