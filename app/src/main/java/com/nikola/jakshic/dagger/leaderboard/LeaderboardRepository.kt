package com.nikola.jakshic.dagger.leaderboard

import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.LeaderboardQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepository @Inject constructor(
    private val dispatchers: Dispatchers,
    private val leaderboardQueries: LeaderboardQueries,
    private val service: OpenDotaService,
    private val leaderboardUrlProvider: LeaderboardUrlProvider
) {
    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getLeaderboardFlow(region: String): Flow<List<LeaderboardUI>> {
        return leaderboardQueries.selectAll(region)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { it.mapToUi() }
            .flowOn(dispatchers.io)
    }

    /**
     * Fetches the leaderboard from the network, takes the first 100 players
     * and inserts them into database.
     *
     * Whenever the database is updated, the observers of [Flow]
     * returned by [getLeaderboardFlow] are notified.
     */
    suspend fun fetchLeaderboard(region: Region): Unit = withContext(dispatchers.io) {
        val url = leaderboardUrlProvider.get()
            ?: throw RuntimeException("Leaderboard url is null")
        val leaderboard = service.getLeaderboard(url, region.name.lowercase()).leaderboard
            ?: throw Exception()
        val list = leaderboard.take(100)
        if (list.isNotEmpty()) {
            // We don't have players ids, we only have their names,
            // if the player has changed his name in the meantime,
            // that would result into 2 rows in the database for a single player.
            // So we need to remove all players from the database and then insert
            // the fresh ones.
            leaderboardQueries.transaction {
                leaderboardQueries.deleteAllByRegion(region.name)
                list.forEach { leaderboardQueries.insert(it.name, region.name) }
            }
        }
    }
}
