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

    private val _streams = MutableLiveData<List<StreamUI>>()
    val streams: LiveData<List<StreamUI>>
        get() = _streams

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val onSuccess: (List<StreamUI>) -> Unit = {
        _status.value = Status.SUCCESS
        _streams.value = it
    }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    private var initialFetch = false

    fun initialFetch() {
        if (!initialFetch) {
            initialFetch = true
            getStreams()
        }
    }

    fun getStreams() {
        _status.value = Status.LOADING
        launch {
            repository.getStreams(onSuccess, onError)
        }
    }
}