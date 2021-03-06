package com.nikola.jakshic.dagger.matchstats

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.sqldelight.MatchBookmarkQueries
import com.nikola.jakshic.dagger.profile.matches.MatchRepository
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchStatsViewModel @Inject constructor(
    private val repository: MatchRepository,
    private val matchBookmarkQueries: MatchBookmarkQueries
) : ScopedViewModel() {

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
            launch {
                repository.getMatchStatsFlow(id)
                    .collectLatest { _match.value = it }
            }
            launch {
                matchBookmarkQueries.isBookmarked(id)
                    .asFlow()
                    .mapToOne(Dispatchers.IO)
                    .collectLatest { _isBookmarked.value = it }
            }
            fetchMatchStats(id)
        }
    }

    fun fetchMatchStats(id: Long) {
        launch {
            _status.value = Status.LOADING
            repository.fetchMatchStats(id, onSuccess, onError)
        }
    }

    fun addToBookmark(matchId: Long, onSuccess: () -> Unit) {
        launch {
            try {
                withContext(Dispatchers.IO) { matchBookmarkQueries.insert(matchId) }
                onSuccess()
            } catch (e: SQLiteConstraintException) {
                // Trying to add to the bookmark but match_stats is not yet added to the database.
            }
        }
    }

    fun removeFromBookmark(matchId: Long) {
        launch {
            withContext(Dispatchers.IO) { matchBookmarkQueries.delete(matchId) }
        }
    }
}