package com.nikola.jakshic.dagger.profile.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.MatchQueries
import com.nikola.jakshic.dagger.common.sqldelight.MatchStatsQueries
import com.nikola.jakshic.dagger.common.sqldelight.PlayerStatsQueries
import com.nikola.jakshic.dagger.matchstats.MatchStatsUI
import com.nikola.jakshic.dagger.matchstats.mapToDb
import com.nikola.jakshic.dagger.matchstats.mapToUi
import com.nikola.jakshic.dagger.profile.matches.byhero.MatchesByHeroDataSourceFactory
import com.nikola.jakshic.dagger.profile.matches.byhero.PagedResponse
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
    private val service: OpenDotaService,
    private val matchQueries: MatchQueries,
    private val matchStatsQueries: MatchStatsQueries,
    private val playerStatsQueries: PlayerStatsQueries,
    private val dispatchers: Dispatchers
) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getMatchesLiveData(
        scope: CoroutineScope,
        factory: DataSource.Factory<Int, MatchUI>,
        id: Long
    ): Response {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(40)
            .setPageSize(20)
            .setPrefetchDistance(5)
            .build()

        val boundaryCallback = MatchBoundaryCallback(scope, service, matchQueries, dispatchers, id)

        val pagedList = LivePagedListBuilder(factory, config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return Response(
            pagedList = pagedList,
            status = boundaryCallback.status,
            retry = { boundaryCallback.retry() }
        )
    }

    /**
     * Fetches the matches from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getMatchesLiveData] are notified.
     */
    suspend fun fetchMatches(id: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            withContext(dispatchers.io) {
                val count = matchQueries.countMatches(id).executeAsOne()
                val list = if (count != 0L) {
                    // There are already some matches in the database
                    // we want to refresh all of them
                    service.getMatches(id, count.toInt(), 0)
                } else {
                    // There are no matches in the database,
                    // we want to fetch only 20 from the network
                    service.getMatches(id, 20, 0)
                }

                if (list.isNotEmpty()) {
                    matchQueries.transaction {
                        matchQueries.deleteAll(id)
                        list.forEach { matchQueries.insert(it.mapToDb(accountId = id)) }
                    }
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }

    fun fetchMatchesByHero(accountId: Long, heroId: Long): PagedResponse<MatchUI> {
        val sourceFactory = MatchesByHeroDataSourceFactory(accountId, heroId, service, dispatchers)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(60)
            .setPageSize(20)
            .setPrefetchDistance(15)
            .build()
        val livePagedList = LivePagedListBuilder(sourceFactory, config)
            .build()

        return PagedResponse(
            pagedList = livePagedList,
            status = Transformations.switchMap(sourceFactory.sourceLiveData) { it.status },
            refresh = { sourceFactory.sourceLiveData.value?.invalidate() },
            retry = { sourceFactory.sourceLiveData.value?.retry() }
        )
    }

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
    suspend fun fetchMatchStats(matchId: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            withContext(dispatchers.io) {
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
                        matchId = matchDb.match_id
                    )
                    match.players?.forEach { playerStatsQueries.insert(it.mapToDb()) }
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }
}
