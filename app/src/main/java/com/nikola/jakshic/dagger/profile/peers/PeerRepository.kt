package com.nikola.jakshic.dagger.profile.peers

import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.PeerQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeerRepository @Inject constructor(
    private val service: OpenDotaService,
    private val peerQueries: PeerQueries
) {

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getPeersFlowByGames(id: Long) =
        peerQueries.selectAllByGames(id, ::mapToUi)
            .asFlow()
            .mapToList(Dispatchers.IO)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getPeersFlowByWinrate(id: Long) =
        peerQueries.selectAllByWinrate(id, ::mapToUi)
            .asFlow()
            .mapToList(Dispatchers.IO)

    /**
     * Fetches the peers from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [Flow]
     * returned by [getPeersFlowByGames] and [getPeersFlowByWinrate] are notified.
     */
    suspend fun fetchPeers(id: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                val peers = service.getPeers(id)
                val list = peers.filter { it.withGames != 0 } // filter opponents from the peer list
                list.map {
                    it.accountId = id // response from the network doesn't contain any information
                    it // about whose this peers are, so we need to set this manually
                }
                peerQueries.transaction {
                    list.forEach { peerQueries.insert(it.mapToDb()) }
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }
}