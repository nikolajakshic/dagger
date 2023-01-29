package com.nikola.jakshic.dagger.common.network

import com.nikola.jakshic.dagger.stream.StreamWrapperJson
import retrofit2.http.GET
import retrofit2.http.Headers

interface TwitchService {
    companion object {
        const val BASE_URL = "https://dagger-proxy-twitch.nikolajaksic.com/"
    }

    @GET("streams")
    @Headers("Client-DPT: dagger")
    suspend fun getStreams(): StreamWrapperJson
}
