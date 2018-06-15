package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.LiveData
import com.nikola.jakshic.dagger.Dispatcher.IO
import com.nikola.jakshic.dagger.data.local.PeerDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeerRepository @Inject constructor(
        private val service: OpenDotaService,
        private val dao: PeerDao) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getPeersLiveDataByGames(id: Long) = dao.getByGames(id)

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getPeersLiveDataByWinrate(id: Long) = dao.getByWinrate(id)

    /**
     * Fetches the peers from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getPeersLiveDataByGames] and [getPeersLiveDataByWinrate] are notified.
     *
     * @param onSuccess called on main thread
     * @param onError called on main thread
     */
    fun fetchPeers(job: Job, id: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        launch(UI, parent = job) {
            try {
                val peers = service.getPeers(id).await()
                withContext(IO) {
                    val list = peers.filter { it.withGames != 0 }   // filter opponents from the peer list
                    list.map {
                        it.accountId = id   // response from the network doesn't contain any information
                        it            // about whose this peers are, so we need to set this manually
                    }
                    dao.insertPeers(list)
                }
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }
}