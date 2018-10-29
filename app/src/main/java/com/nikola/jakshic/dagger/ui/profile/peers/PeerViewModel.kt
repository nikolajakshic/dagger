package com.nikola.jakshic.dagger.ui.profile.peers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.repository.PeerRepository
import com.nikola.jakshic.dagger.ui.ScopedViewModel
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Peer
import kotlinx.coroutines.launch
import javax.inject.Inject

class PeerViewModel @Inject constructor(private val repository: PeerRepository) : ScopedViewModel() {

    lateinit var list: LiveData<List<Peer>>
        private set

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
            list = repository.getPeersLiveDataByGames(id)
        }
    }

    fun fetchPeers(id: Long) {
        launch {
            _status.value = Status.LOADING
            repository.fetchPeers(id, onSuccess, onError)
        }
    }

    fun sortByGames(id: Long) {
        list = repository.getPeersLiveDataByGames(id)
    }

    fun sortByWinRate(id: Long) {
        list = repository.getPeersLiveDataByWinrate(id)
    }
}