package com.nikola.jakshic.dagger.ui.profile.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.repository.MatchRepository
import com.nikola.jakshic.dagger.ui.ScopedViewModel
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Match
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val repository: MatchRepository) : ScopedViewModel() {

    private lateinit var response: Response

    val list: LiveData<PagedList<Match>>
        get() = response.pagedList

    private val _refreshStatus = MutableLiveData<Status>()
    val refreshStatus: LiveData<Status>
        get() = _refreshStatus

    val loadMoreStatus: LiveData<Status>
        get() = response.status

    private var initialFetch = false

    private val onSuccess: () -> Unit = { _refreshStatus.value = Status.SUCCESS }
    private val onError: () -> Unit = { _refreshStatus.value = Status.ERROR }

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            response = repository.getMatchesLiveData(this, id)
            fetchMatches(id)
        }
    }

    fun fetchMatches(id: Long) {
        launch {
            _refreshStatus.value = Status.LOADING
            repository.fetchMatches(id, onSuccess, onError)
        }
    }

    fun retry() = response.retry()
}