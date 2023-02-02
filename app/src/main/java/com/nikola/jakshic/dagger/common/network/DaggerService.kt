package com.nikola.jakshic.dagger.common.network

import com.nikola.jakshic.dagger.leaderboard.RemoteConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming

interface DaggerService {
    companion object {
        const val BASE_URL = "https://dagger.nikolajaksic.com/"
    }

    @GET("remote-config.json")
    fun getRemoteConfig(): Call<RemoteConfig>

    @Streaming
    @GET("static/items.zip")
    fun getItemsAssets(): Call<ResponseBody>

    @Streaming
    @GET("static/heroes.zip")
    fun getHeroesAssets(): Call<ResponseBody>
}
