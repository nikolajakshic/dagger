package com.nikola.jakshic.dagger.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.nikola.jakshic.dagger.BuildConfig
import com.nikola.jakshic.dagger.common.network.NullPrimitiveAdapter
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.network.Tls12SocketFactory
import com.nikola.jakshic.dagger.common.network.TwitchAuthentication
import com.nikola.jakshic.dagger.common.network.TwitchService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.security.KeyStore
import java.util.ArrayList
import java.util.Arrays
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Module
class NetworkModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(NullPrimitiveAdapter())
            .build()
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
    fun provideTwitchInterceptor(moshi: Moshi, sharedPreferences: SharedPreferences): Interceptor {
        return Interceptor {
            val originalRequest = it.request()

            if (originalRequest.url().host() != "api.twitch.tv") {
                return@Interceptor it.proceed(originalRequest)
            }

            val prefAccessToken = "twitch_access_token"
            val accessToken = sharedPreferences.getString(prefAccessToken, null)
            // Attach Authorization token.
            val twitchResponse = it.proceed(originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build())

            if (twitchResponse.code() != 401) {
                return@Interceptor twitchResponse
            }
            // Either we are communicating with Twitch for the first time, so we don't have saved
            // token, or the token has expired. Either way, we need to get the new one.
            val url = """
                    https://id.twitch.tv/oauth2/token
                    ?client_id=${BuildConfig.TWITCH_CLIENT_ID}
                    &client_secret=${BuildConfig.TWITCH_SECRET}
                    &grant_type=client_credentials
                """.trimIndent()
            val tokenRequest = Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(null, ""))
                .build()
            val tokenResponse = it.proceed(tokenRequest)
            if (tokenResponse.isSuccessful && tokenResponse.body() != null) {
                val tokenBody = tokenResponse.body()!!.string()
                val token = moshi.adapter(TwitchAuthentication::class.java)
                    .fromJson(tokenBody)!!
                    .token
                // Interceptor is already on another thread, so we use commit instead of apply.
                sharedPreferences.edit().putString(prefAccessToken, token).commit()
            }

            val updatedToken = sharedPreferences.getString(prefAccessToken, null)
            return@Interceptor it.proceed(originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $updatedToken")
                .build())
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(twitchInterceptor: Interceptor): OkHttpClient {
        val cacheDir = File(context.cacheDir, "okhttp-cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val clientBuilder = OkHttpClient.Builder()
            .cache(Cache(cacheDir, (30 * 1024 * 1024).toLong()))
            .readTimeout(35, TimeUnit.SECONDS)
            .addInterceptor(twitchInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        }

        val cipherSuites = ArrayList<CipherSuite>()
        cipherSuites.addAll(ConnectionSpec.MODERN_TLS.cipherSuites()!!)
        cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA)
        cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)

        val legacyTls = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .cipherSuites(*cipherSuites.toTypedArray())
            .build()

        // Add obsolete Cipher Suites because the host we are trying to reach doesn't have the right ones
        clientBuilder.connectionSpecs(listOf(legacyTls, ConnectionSpec.CLEARTEXT))

        if (Build.VERSION.SDK_INT in 16..21) {
            try {
                val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)

                val trustManagers = trustManagerFactory.trustManagers
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                    throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
                }

                val trustManager = trustManagers[0] as X509TrustManager

                val sslContext = SSLContext.getInstance("TLSv1.2")
                sslContext.init(null, null, null)

                // Enable TLS 1.2 for older devices
                clientBuilder.sslSocketFactory(Tls12SocketFactory(sslContext.socketFactory), trustManager)
            } catch (e: Exception) {
                Log.e("NetworkModule", "Error while setting TLS 1.2: ", e)
            }
        }

        return clientBuilder.build()
    }
}