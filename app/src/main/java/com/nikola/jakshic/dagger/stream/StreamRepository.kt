package com.nikola.jakshic.dagger.stream

import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.TwitchService
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamRepository @Inject constructor(
    private val dispatchers: Dispatchers,
    private val service: TwitchService,
) {
    suspend fun getStreams(): List<StreamUI> = withContext(dispatchers.io) {
        return@withContext service.getStreams().stream.map(StreamJson::mapToUi)
    }
}
