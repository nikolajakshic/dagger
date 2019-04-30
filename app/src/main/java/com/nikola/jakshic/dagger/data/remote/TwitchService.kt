package com.nikola.jakshic.dagger.data.remote

import com.nikola.jakshic.dagger.BuildConfig
import com.nikola.jakshic.dagger.vo.StreamBox
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TwitchService {

    companion object {
        const val BASE_URL = "https://api.twitch.tv/helix/"
    }

    @GET("streams?game_id=29595")
    @Headers(BuildConfig.TWITCH_HEADER)
    suspend fun getStreams(@Query("first") limit: Int): StreamBox
}