package com.nikola.jakshic.dagger.profile.peers

import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.PeerQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
        peerQueries.selectAllByGames(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.mapToUi() }
            .flowOn(Dispatchers.IO)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getPeersFlowByWinrate(id: Long) =
        peerQueries.selectAllByWinrate(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.mapToUi() }
            .flowOn(Dispatchers.IO)

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
                val list = peers.filter { it.withGames != 0L } // filter opponents from the peer list
                peerQueries.transaction {
                    list.forEach { peerQueries.insert(it.mapToDb(accountId = id)) }
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }
}
