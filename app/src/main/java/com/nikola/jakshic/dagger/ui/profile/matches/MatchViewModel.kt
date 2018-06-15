package com.nikola.jakshic.dagger.ui.profile.matches

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.nikola.jakshic.dagger.repository.MatchRepository
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Match
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelChildren
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val repository: MatchRepository) : ViewModel() {

    lateinit var list: LiveData<PagedList<Match>>
        private set
    val status = MutableLiveData<Status>()
    private var initialFetch = false
    private val jobs = Job()
    private val onLoading: () -> Unit = { status.value = Status.LOADING }
    private val onSuccess: () -> Unit = { status.value = Status.SUCCESS }
    private val onError: () -> Unit = { status.value = Status.ERROR }

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            list = repository.getMatchesLiveData(jobs, id, onLoading, onSuccess, onError)
            fetchMatches(id)
        }
    }

    fun fetchMatches(id: Long) {
        status.value = Status.LOADING
        repository.fetchMatches(jobs,id, onSuccess, onError)
    }

    override fun onCleared() {
        jobs.cancelChildren()
    }
}