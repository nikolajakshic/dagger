package com.nikola.jakshic.dagger.common.network

import com.nikola.jakshic.dagger.leaderboard.RemoteConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DaggerService {
    companion object {
        const val BASE_URL = "https://dagger.nikolajaksic.com/"
    }

    @GET("remote-config.json")
    fun getRemoteConfig(): Call<RemoteConfig>

    @GET
    @Streaming
    fun getAssets(@Url url: String): Call<ResponseBody>
}
