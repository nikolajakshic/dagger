package com.nikola.jakshic.dagger.stream

import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class StreamViewModel @Inject constructor(
    private val repository: StreamRepository
) : ScopedViewModel() {
    private val isInitial = AtomicBoolean(true)

    private val _streams = MutableStateFlow<List<StreamUI>>(emptyList())
    val streams: StateFlow<List<StreamUI>> = _streams

    private val _status = MutableStateFlow(Status.LOADING)
    val status: StateFlow<Status> = _status

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun initialFetch() {
        if (isInitial.compareAndSet(true, false)) {
            getStreams()
        }
    }

    fun getStreams() {
        launch {
            try {
                _isLoading.value = true
                _status.value = Status.LOADING
                val items = repository.getStreams()
                _streams.value = items
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                _status.value = Status.ERROR
            } finally {
                _isLoading.value = false
            }
        }
    }
}
