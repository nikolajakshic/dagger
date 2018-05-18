package com.nikola.jakshic.dagger.ui.matchStats

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.repository.MatchRepository
import com.nikola.jakshic.dagger.vo.Stats
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MatchStatsViewModel @Inject constructor(
        private val repository: MatchRepository) : ViewModel() {

    lateinit var match: LiveData<Stats>
        private set
    private val disposables = CompositeDisposable()
    private var initialFetch = false

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            match = repository.getMatchStatsLiveData(id)
            fetchMatchStats(id)
        }
    }

    fun fetchMatchStats(id: Long) {
        disposables.add(repository.fetchMatchStats(id))
    }

    override fun onCleared() {
        disposables.clear()
    }
}