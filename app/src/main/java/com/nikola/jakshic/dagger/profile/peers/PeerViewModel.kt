package com.nikola.jakshic.dagger.profile.peers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val KEY_SORT_BY = "sort-by"

@HiltViewModel
class PeerViewModel @Inject constructor(
    private val repository: PeerRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val accountId = PeerFragment.getAccountId(savedStateHandle)

    private val _sortBy = savedStateHandle.getStateFlow(KEY_SORT_BY, SortBy.GAMES.name)
    val sortBy get() = SortBy.valueOf(_sortBy.value)

    val list: StateFlow<List<PeerUI>> = _sortBy.flatMapLatest {
        when (SortBy.valueOf(it)) {
            SortBy.GAMES -> repository.getPeersByGames(accountId)
            SortBy.WINRATE -> repository.getPeersByWinrate(accountId)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchPeers()
    }

    fun fetchPeers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.fetchPeers(accountId)
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sortBy(sortBy: SortBy) {
        savedStateHandle[KEY_SORT_BY] = sortBy.name
    }
}
