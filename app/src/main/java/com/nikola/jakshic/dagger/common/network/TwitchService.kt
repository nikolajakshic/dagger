package com.nikola.jakshic.dagger.common.network

import com.nikola.jakshic.dagger.BuildConfig
import com.nikola.jakshic.dagger.stream.StreamBox
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TwitchService {

    companion object {
        const val BASE_URL = "https://api.twitch.tv/helix/"
    }

    @GET("streams?game_id=29595")
    @Headers("Client-ID: ${BuildConfig.TWITCH_CLIENT_ID}")
    suspend fun getStreams(@Query("first") limit: Int): StreamBox
}