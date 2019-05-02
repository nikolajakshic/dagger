package com.nikola.jakshic.dagger.repository

import com.nikola.jakshic.dagger.data.remote.TwitchService
import com.nikola.jakshic.dagger.vo.Stream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StreamRepository @Inject constructor(private val service: TwitchService) {

    suspend fun getStreams(limit: Int, onSuccess: (list: List<Stream>) -> Unit, onError: () -> Unit) {
        try {
            val list = withContext(Dispatchers.IO) { service.getStreams(limit) }
            onSuccess(list.stream)
        } catch (e: Exception) {
            onError()
        }
    }
}