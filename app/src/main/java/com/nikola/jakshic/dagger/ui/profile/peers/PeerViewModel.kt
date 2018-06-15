package com.nikola.jakshic.dagger.ui.profile.peers

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.repository.PeerRepository
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Peer
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelChildren
import javax.inject.Inject

class PeerViewModel @Inject constructor(private val repository: PeerRepository) : ViewModel() {

    lateinit var list: LiveData<List<Peer>>
        private set
    val status = MutableLiveData<Status>()
    private var initialFatch = false
    private val jobs = Job()
    private val onSuccess: () -> Unit = { status.value = Status.SUCCESS }
    private val onError: () -> Unit = { status.value = Status.ERROR }

    fun initialFetch(id: Long) {
        if (!initialFatch) {
            initialFatch = true
            fetchPeers(id)
            list = repository.getPeersLiveDataByGames(id)
        }
    }

    fun fetchPeers(id: Long) {
        status.value = Status.LOADING
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