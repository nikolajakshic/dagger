package com.nikola.jakshic.dagger.stream

import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.TwitchService
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StreamRepository @Inject constructor(
    private val dispatchers: Dispatchers,
    private val service: TwitchService
) {
    suspend fun getStreams(): List<StreamUI> {
        val streams = withContext(dispatchers.io) { service.getStreams().stream }
        return withContext(dispatchers.default) { streams.map(StreamJson::mapToUi) }
    }
}