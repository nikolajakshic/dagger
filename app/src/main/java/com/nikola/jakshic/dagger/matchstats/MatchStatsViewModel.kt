package com.nikola.jakshic.dagger.matchstats

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.sqldelight.MatchBookmarkQueries
import com.nikola.jakshic.dagger.profile.matches.MatchRepository
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchStatsViewModel @Inject constructor(
    private val repository: MatchRepository,
    private val matchBookmarkQueries: MatchBookmarkQueries,
    private val dispatchers: Dispatchers
) : ViewModel() {
    private val _match = MutableLiveData<MatchStatsUI>()
    val match: LiveData<MatchStatsUI>
        get() = _match

    private val _isBookmarked = MutableLiveData<Long>()
    val isBookmarked: LiveData<Long>
        get() = _isBookmarked

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var initialFetch = false

    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            viewModelScope.launch {
                repository.getMatchStatsFlow(id)
                    .collectLatest { _match.value = it }
            }
            viewModelScope.launch {
                matchBookmarkQueries.isBookmarked(id)
                    .asFlow()
                    .mapToOne(dispatchers.io)
                    .collectLatest { _isBookmarked.value = it }
            }
            fetchMatchStats(id)
        }
    }

    fun fetchMatchStats(id: Long) {
        viewModelScope.launch {
            _status.value = Status.LOADING
            repository.fetchMatchStats(id, onSuccess, onError)
        }
    }

    fun addToBookmark(matchId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(dispatchers.io) { matchBookmarkQueries.insert(matchId) }
                onSuccess()
            } catch (e: SQLiteConstraintException) {
                // Trying to add to the bookmark but match_stats is not yet added to the database.
            }
        }
    }

    fun removeFromBookmark(matchId: Long) {
        viewModelScope.launch {
            withContext(dispatchers.io) { matchBookmarkQueries.delete(matchId) }
        }
    }
}
