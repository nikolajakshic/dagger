package com.nikola.jakshic.dagger.common.network

import com.nikola.jakshic.dagger.leaderboard.RemoteConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

interface DaggerService {
    companion object {
        const val BASE_URL = "https://dagger.nikolajaksic.com/"
    }

    @GET("remote-config.json")
    suspend fun getRemoteConfig(): RemoteConfig

    @Streaming
    @GET("static/items.zip")
    suspend fun getItemsAssets(): ResponseBody

    @Streaming
    @GET("static/heroes.zip")
    suspend fun getHeroesAssets(): ResponseBody
}
