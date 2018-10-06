package com.nikola.jakshic.dagger.ui.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikola.jakshic.dagger.repository.LeaderboardRepository
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Leaderboard
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelChildren
import javax.inject.Inject

class RegionViewModel @Inject constructor(
        private val repository: LeaderboardRepository) : ViewModel() {

    lateinit var list: LiveData<List<Leaderboard>>
        private set
    val status = MutableLiveData<Status>()
    private var initialFetch = false
    private val jobs = Job()
    private val onSuccess: () -> Unit = { status.value = Status.SUCCESS }
    private val onError: () -> Unit = { status.value = Status.ERROR }

    fun initialFetch(region: String) {
        if (!initialFetch) {
            initialFetch = true
            list = repository.getLeaderboardLiveData(region)
            fetchLeaderboard(region)
        }
    }

    fun fetchLeaderboard(region: String) {
        status.value = Status.LOADING
        repository.fetchLeaderboard(jobs, region, onSuccess, onError)
    }

    override fun onCleared() {
        jobs.cancelChildren()
    }
}