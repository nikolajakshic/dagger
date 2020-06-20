package com.nikola.jakshic.dagger.profile.peers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PeerViewModel @Inject constructor(private val repository: PeerRepository) : ScopedViewModel() {
    private val _list = MutableLiveData<List<PeerUI>>()
    val list: LiveData<List<PeerUI>>
        get() = _list

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var initialFetch = false

    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            fetchPeers(id)
            sortByGames(id)
        }
    }

    fun fetchPeers(id: Long) {
        launch {
            _status.value = Status.LOADING
            repository.fetchPeers(id, onSuccess, onError)
        }
    }

    fun sortByGames(id: Long) {
        launch {
            repository.getPeersFlowByGames(id)
                .collectLatest { _list.value = it }
        }
    }

    fun sortByWinRate(id: Long) {
        launch {
            repository.getPeersFlowByWinrate(id)
                .collectLatest { _list.value = it }
        }
    }
}