package com.nikola.jakshic.dagger.matchstats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.profile.matches.MatchRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MatchStatsViewModel @Inject constructor(
    private val repository: MatchRepository
) : ScopedViewModel() {

    lateinit var match: LiveData<Stats>
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
            fetchMatchStats(id)
        }
    }

    fun fetchMatchStats(id: Long) {
        launch {
            _status.value = Status.LOADING
            repository.fetchMatchStats(id, onSuccess, onError)
        }
    }
}