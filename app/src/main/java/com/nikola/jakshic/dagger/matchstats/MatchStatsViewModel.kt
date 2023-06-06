package com.nikola.jakshic.dagger.matchstats

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.sqldelight.MatchBookmarkQueries
import com.nikola.jakshic.dagger.profile.matches.MatchRepository
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MatchStatsViewModel @Inject constructor(
    private val repository: MatchRepository,
    private val matchBookmarkQueries: MatchBookmarkQueries,
    private val dispatchers: Dispatchers,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val matchId = MatchStatsFragmentArgs.fromSavedStateHandle(savedStateHandle).matchId

    val match = repository.getMatchStatsFlow(matchId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    val isBookmarked = matchBookmarkQueries.isBookmarked(matchId)
        .asFlow()
        .mapToOne(dispatchers.io)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L,
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _successfullyBookmarked = Channel<Unit>(Channel.CONFLATED)
    val successfullyBookmarked = _successfullyBookmarked.receiveAsFlow()

    init {
        fetchMatchStats()
    }

    fun fetchMatchStats() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.fetchMatchStats(matchId)
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToBookmark() {
        viewModelScope.launch {
            try {
                withContext(dispatchers.io) { matchBookmarkQueries.insert(matchId) }
                _successfullyBookmarked.send(Unit)
            } catch (e: SQLiteConstraintException) {
                // Trying to add to the bookmark but match_stats is not yet added to the database.
            }
        }
    }

    fun removeFromBookmark() {
        viewModelScope.launch {
            withContext(dispatchers.io) { matchBookmarkQueries.delete(matchId) }
        }
    }
}
