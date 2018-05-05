package com.nikola.jakshic.dagger.ui.match

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.vo.MatchStats
import com.nikola.jakshic.dagger.repository.MatchRepository
import javax.inject.Inject

class MatchDetailViewModel @Inject constructor(
        val repository: MatchRepository) : ViewModel() {

    val match = MutableLiveData<MatchStats>()
    val loading = MutableLiveData<Boolean>()
    private var initialFetch = false

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            repository.fetchMatchData(match, loading, id)
        }
    }
}