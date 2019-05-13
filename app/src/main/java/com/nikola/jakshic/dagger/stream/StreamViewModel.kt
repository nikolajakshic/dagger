package com.nikola.jakshic.dagger.stream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import kotlinx.coroutines.launch
import javax.inject.Inject

class StreamViewModel @Inject constructor(
    private val repository: StreamRepository
) : ScopedViewModel() {

    private val _streams = MutableLiveData<List<Stream>>()
    val streams: LiveData<List<Stream>>
        get() = _streams

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val onSuccess: (List<Stream>) -> Unit = {
        _status.value = Status.SUCCESS
        _streams.value = it
    }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    private var initialFetch = false

    fun initialFetch(limit: Int) {
        if (!initialFetch) {
            initialFetch = true
            getStreams(limit)
        }
    }

    fun getStreams(limit: Int) {
        _status.value = Status.LOADING
        launch {
            repository.getStreams(limit, onSuccess, onError)
        }
    }
}