package com.nikola.jakshic.dagger.ui.profile.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.repository.MatchRepository
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Match
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelChildren
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val repository: MatchRepository) : ViewModel() {

    lateinit var list: LiveData<PagedList<Match>>
        private set
    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status
    private var initialFetch = false
    private val jobs = Job()
    private val onLoading: () -> Unit = { _status.value = Status.LOADING }
    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            list = repository.getMatchesLiveData(jobs, id, onLoading, onSuccess, onError)
            fetchMatches(id)
        }
    }

    fun fetchMatches(id: Long) {
        _status.value = Status.LOADING
        repository.fetchMatches(jobs, id, onSuccess, onError)
    }

    override fun onCleared() {
        jobs.cancelChildren()
    }
}