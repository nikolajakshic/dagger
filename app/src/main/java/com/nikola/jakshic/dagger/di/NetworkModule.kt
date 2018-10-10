package com.nikola.jakshic.dagger.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec


@Module
class NetworkModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
    }

    @Provides
    @Singleton
    fun provideOpenDotaService(okHttpClient: OkHttpClient, gson: Gson): OpenDotaService {
        return Retrofit.Builder()
                .baseUrl(OpenDotaService.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
                .create(OpenDotaService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val cipherSuites = ArrayList<CipherSuite>()
        cipherSuites.addAll(ConnectionSpec.MODERN_TLS.cipherSuites()!!)
        cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA)
        cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)

        val legacyTls = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .cipherSuites(*cipherSuites.toTypedArray())
                .build()

        val clientBuilder = OkHttpClient.Builder()
                .cache(Cache(context.cacheDir, (10 * 1000 * 1000).toLong()))
                .readTimeout(35, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .connectionSpecs(listOf(legacyTls, ConnectionSpec.CLEARTEXT))

        return clientBuilder.build()
    }
}