package com.nikola.jakshic.dagger.di

import android.content.Context
import com.nikola.jakshic.dagger.BuildConfig
import com.nikola.jakshic.dagger.common.network.DaggerService
import com.nikola.jakshic.dagger.common.network.NullPrimitiveAdapter
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.network.TwitchService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(NullPrimitiveAdapter())
            .build()
    }

    @Provides
    @Singleton
    fun provideDaggerService(okHttpClient: OkHttpClient, moshi: Moshi): DaggerService {
        return Retrofit.Builder()
            .baseUrl(DaggerService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(DaggerService::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenDotaService(okHttpClient: OkHttpClient, moshi: Moshi): OpenDotaService {
        return Retrofit.Builder()
            .baseUrl(OpenDotaService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(OpenDotaService::class.java)
    }

    @Provides
    @Singleton
    fun provideTwitchService(okHttpClient: OkHttpClient, moshi: Moshi): TwitchService {
        return Retrofit.Builder()
            .baseUrl(TwitchService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(TwitchService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cacheDir = File(context.cacheDir, "okhttp-cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val clientBuilder = OkHttpClient.Builder()
            .cache(Cache(cacheDir, (30 * 1024 * 1024).toLong()))
            .readTimeout(35, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        }

        return clientBuilder.build()
    }
}
