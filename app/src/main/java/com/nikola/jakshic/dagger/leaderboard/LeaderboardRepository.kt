package com.nikola.jakshic.dagger.leaderboard

import androidx.lifecycle.LiveData
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.LeaderboardQueries
import com.nikola.jakshic.dagger.common.sqldelight.Leaderboards
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepository @Inject constructor(
    private val leaderboardQueries: LeaderboardQueries,
    private val service: OpenDotaService
) {

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getLeaderboardFlow(region: String): Flow<List<LeaderboardUI>> {
        return leaderboardQueries.selectAll(region)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.mapToUi() }
    }

    /**
     * Fetches the leaderboard from the network, takes the first 100 players
     * and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getLeaderboardFlow] are notified.
     */
    suspend fun fetchLeaderboard(region: String, onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                val leaderboard = service.getLeaderboard(region).leaderboard
                    ?: throw Exception()
                val list = leaderboard.take(100)
                list.map {
                    it.region = region // response from the network doesn't contain any information
                    it // about the region, so we need to set this manually
                }
                if (list.isNotEmpty()) {
                    // We don't have players ids, we only have their names,
                    // if the player has changed his name in the meantime,
                    // that would result into 2 rows in the database for a single player.
                    // So we need to remove all players from the database and then insert
                    // the fresh ones.
                    leaderboardQueries.transaction {
                        leaderboardQueries.deleteAllByRegion(region)
                        list.forEach {
                            // ID is auto-incrementing, the value we passed here is irrelevant.
                            leaderboardQueries.insert(Leaderboards(-1, it.name, it.region))
                        }
                    }
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }
}