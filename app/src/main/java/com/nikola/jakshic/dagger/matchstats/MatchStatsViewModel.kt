package com.nikola.jakshic.dagger.matchstats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.bookmark.match.MatchBookmark
import com.nikola.jakshic.dagger.bookmark.match.MatchBookmarkDao
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.profile.matches.MatchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchStatsViewModel @Inject constructor(
    private val repository: MatchRepository,
    private val matchBookmarkDao: MatchBookmarkDao
) : ScopedViewModel() {

    lateinit var match: LiveData<Stats>
        private set

    lateinit var isBookmarked: LiveData<Long>
        private set

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var initialFetch = false

    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            match = repository.getMatchStatsLiveData(id)
            isBookmarked = matchBookmarkDao.isMatchBookmarked(id)
            fetchMatchStats(id)
        }
    }

    fun fetchMatchStats(id: Long) {
        launch {
            _status.value = Status.LOADING
            repository.fetchMatchStats(id, onSuccess, onError)
        }
    }

    fun addToBookmark(matchId: Long) {
        launch {
            withContext(Dispatchers.IO) { matchBookmarkDao.addToBookmark(MatchBookmark(matchId)) }
        }
    }

    fun removeFromBookmark(matchId: Long) {
        launch {
            withContext(Dispatchers.IO) { matchBookmarkDao.removeFromBookmark(matchId) }
        }
    }
}