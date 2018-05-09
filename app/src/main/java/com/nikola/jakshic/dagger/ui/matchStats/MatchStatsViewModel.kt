package com.nikola.jakshic.dagger.ui.matchStats

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.data.local.MatchStatsDao
import com.nikola.jakshic.dagger.repository.MatchRepository
import com.nikola.jakshic.dagger.vo.Stats
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MatchStatsViewModel @Inject constructor(
        private val repository: MatchRepository,
        private val dao: MatchStatsDao) : ViewModel() {

    lateinit var match: LiveData<Stats>
        private set
    val loading = MutableLiveData<Boolean>()
    private val disposables = CompositeDisposable()
    private var initialFetch = false

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            match = dao.getMatchStats(id)
            fetchMatchStats(id)
        }
    }

    fun fetchMatchStats(id: Long) {
        disposables.add(repository.fetchMatchStats(loading, id))
    }

    override fun onCleared() {
        disposables.clear()
    }
}