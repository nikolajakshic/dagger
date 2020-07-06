package com.nikola.jakshic.dagger.di

import android.content.Context
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class CoilModule {
    @Provides
    @Singleton
    fun provideImageLoader(context: Context, client: OkHttpClient): ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient(client)
            .crossfade(300)
            .build()
    }
}