package com.nikola.jakshic.dagger.stream

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StreamViewModel @Inject constructor(private val repository: StreamRepository) : ViewModel() {
    private val streams = MutableStateFlow(emptyList<StreamUI>())
    private val isLoading = MutableStateFlow(false)
    private val error = MutableStateFlow(false)

    val streamUiState = combine(streams, isLoading, error, ::StreamUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StreamUiState.DEFAULT,
        )

    init {
        fetchStreams()
    }

    fun fetchStreams() {
        viewModelScope.launch {
            try {
                error.value = false
                isLoading.value = true
                streams.value = repository.getStreams()
            } catch (e: Exception) {
                Timber.d(e)
                error.value = true
            } finally {
                isLoading.value = false
            }
        }
    }
}
