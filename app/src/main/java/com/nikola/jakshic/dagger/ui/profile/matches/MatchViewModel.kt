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

    lateinit var list: LiveData<PagedList<Match>>
        private set

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var initialFetch = false

    private val onLoading: () -> Unit = { _status.value = Status.LOADING }
    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            list = repository.getMatchesLiveData(this, id, onLoading, onSuccess, onError)
            fetchMatches(id)
        }
    }

    fun fetchMatches(id: Long) {
        launch {
            _status.value = Status.LOADING
            repository.fetchMatches(id, onSuccess, onError)
        }
    }
}