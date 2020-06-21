package com.nikola.jakshic.dagger.leaderboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegionViewModel @ViewModelInject constructor(
    private val repository: LeaderboardRepository
) : ScopedViewModel() {

    private val _list = MutableLiveData<List<LeaderboardUI>>()
    val list: LiveData<List<LeaderboardUI>>
        get() = _list

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var initialFetch = false

    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun initialFetch(region: String) {
        if (!initialFetch) {
            initialFetch = true
            launch {
                repository.getLeaderboardFlow(region)
                    .collect { _list.value = it }
            }
            fetchLeaderboard(region)
        }
    }

    fun fetchLeaderboard(region: String) {
        launch {
            _status.value = Status.LOADING
            repository.fetchLeaderboard(region, onSuccess, onError)
        }
    }
}