package com.nikola.jakshic.dagger.profile.peers

import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.PeerQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeerRepository @Inject constructor(
    private val service: OpenDotaService,
    private val peerQueries: PeerQueries,
    private val dispatchers: Dispatchers,
) {
    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getPeersByGames(id: Long) =
        peerQueries.selectAllByGames(id)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { it.mapToUi() }
            .flowOn(dispatchers.io)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getPeersByWinrate(id: Long) =
        peerQueries.selectAllByWinrate(id)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { it.mapToUi() }
            .flowOn(dispatchers.io)

    /**
     * Fetches the peers from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [Flow]
     * returned by [getPeersByGames] and [getPeersByWinrate] are notified.
     */
    suspend fun fetchPeers(accountId: Long) {
        val peers = service.getPeers(accountId)
        withContext(dispatchers.io) {
            val list = peers.filter { it.withGames != 0L } // filter opponents from the peer list
            peerQueries.transaction {
                list.forEach { peerQueries.insert(it.mapToDb(accountId = accountId)) }
            }
        }
    }
}
