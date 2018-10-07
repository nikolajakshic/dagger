package com.nikola.jakshic.dagger.ui.profile.peers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikola.jakshic.dagger.repository.PeerRepository
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Peer
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelChildren
import javax.inject.Inject

class PeerViewModel @Inject constructor(private val repository: PeerRepository) : ViewModel() {

    lateinit var list: LiveData<List<Peer>>
        private set
    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status
    private var initialFetch = false
    private val jobs = Job()
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
        _status.value = Status.LOADING
        repository.fetchPeers(jobs, id, onSuccess, onError)
    }

    fun sortByGames(id: Long) {
        list = repository.getPeersLiveDataByGames(id)
    }

    fun sortByWinRate(id: Long) {
        list = repository.getPeersLiveDataByWinrate(id)
    }

    override fun onCleared() {
        jobs.cancelChildren()
    }
}