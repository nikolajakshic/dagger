package com.nikola.jakshic.dagger.ui.profile.peers

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Peer
import com.nikola.jakshic.dagger.repository.PeerRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PeerViewModel @Inject constructor(private val repository: PeerRepository) : ViewModel() {

    lateinit var list: LiveData<List<Peer>>
        private set
    val status = MutableLiveData<Status>()
    private var initialFatch = false
    private val disposables = CompositeDisposable()

    fun initialFetch(id: Long) {
        if (!initialFatch) {
            initialFatch = true
            fetchPeers(id)
            list = repository.sortByGames(id)
        }
    }

    fun fetchPeers(id: Long) {
        disposables.add(repository.fetchPeers(status, id))
    }

    fun sortByGames(id: Long) {
        list = repository.sortByGames(id)
    }

    fun sortByWinRate(id: Long) {
        list = repository.sortByWinRate(id)
    }

    override fun onCleared() {
        disposables.clear()
    }
}