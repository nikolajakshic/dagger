package com.nikola.jakshic.dagger.di

import android.content.Context
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {
    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context, client: OkHttpClient): ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient(client)
            .crossfade(300)
            .build()
    }
}