package com.nikola.jakshic.dagger.ui.matchstats

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nikola.jakshic.dagger.repository.MatchRepository
import com.nikola.jakshic.dagger.vo.Stats
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelChildren
import javax.inject.Inject

class MatchStatsViewModel @Inject constructor(
        private val repository: MatchRepository) : ViewModel() {

    lateinit var match: LiveData<Stats>
        private set
    private val jobs = Job()
    private var initialFetch = false

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            match = repository.getMatchStatsLiveData(id)
            fetchMatchStats(id)
        }
    }

    fun fetchMatchStats(id: Long) {
        repository.fetchMatchStats(jobs, id)
    }

    override fun onCleared() {
        jobs.cancelChildren()
    }
}