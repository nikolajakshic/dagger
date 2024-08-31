package com.nikola.jakshic.dagger.profile.matches

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.MatchStatsQueries
import com.nikola.jakshic.dagger.common.sqldelight.PlayerStatsQueries
import com.nikola.jakshic.dagger.matchstats.MatchStatsUI
import com.nikola.jakshic.dagger.matchstats.mapToDb
import com.nikola.jakshic.dagger.matchstats.mapToUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
    private val service: OpenDotaService,
    private val matchStatsQueries: MatchStatsQueries,
    private val playerStatsQueries: PlayerStatsQueries,
    private val dispatchers: Dispatchers,
) {
    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getMatchStatsFlow(id: Long): Flow<MatchStatsUI?> {
        return matchStatsQueries.selectAllMatchStats(id)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { it.mapToUi() }
            .flowOn(dispatchers.io)
    }

    /**
     * Fetches the match-stats from the network and inserts it into database.
     *
     * Whenever the database is updated, the observers of [Flow]
     * returned by [getMatchStatsFlow] are notified.
     */
    suspend fun fetchMatchStats(matchId: Long): Unit = withContext(dispatchers.io) {
        val match = service.getMatch(matchId)
        matchStatsQueries.transaction {
            val matchDb = match.mapToDb()
            matchStatsQueries.upsert(
                radiantWin = matchDb.radiant_win,
                direScore = matchDb.dire_score,
                radiantScore = matchDb.radiant_score,
                skill = matchDb.skill,
                gameMode = matchDb.game_mode,
                duration = matchDb.duration,
                startTime = matchDb.start_time,
                radiantBarracks = matchDb.radiant_barracks,
                direBarracks = matchDb.dire_barracks,
                radiantTowers = matchDb.radiant_towers,
                direTowers = matchDb.dire_towers,
                radiantName = matchDb.radiant_name,
                direName = matchDb.dire_name,
                matchId = matchDb.match_id,
            )
            match.players?.forEach { playerStatsQueries.insert(it.mapToDb(matchId)) }
        }
    }
}
