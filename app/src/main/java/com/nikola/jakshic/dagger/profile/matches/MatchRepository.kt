package com.nikola.jakshic.dagger.profile.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.database.DotaDatabase
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.paging.QueryDataSourceFactory
import com.nikola.jakshic.dagger.common.sqldelight.MatchQueries
import com.nikola.jakshic.dagger.matchstats.MatchStatsDao
import com.nikola.jakshic.dagger.matchstats.PlayerStatsDao
import com.nikola.jakshic.dagger.matchstats.Stats
import com.nikola.jakshic.dagger.profile.matches.byhero.MatchesByHeroDataSourceFactory
import com.nikola.jakshic.dagger.profile.matches.byhero.PagedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
    private val service: OpenDotaService,
    private val db: DotaDatabase,
    private val matchQueries: MatchQueries,
    private val matchStatsDao: MatchStatsDao,
    private val playerStatsDao: PlayerStatsDao
) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getMatchesLiveData(scope: CoroutineScope, id: Long): Response {
        val queryProvider = { limit: Long, offset: Long ->
            matchQueries.selectAll(id, limit, offset, ::mapToUi)
        }
        val factory = QueryDataSourceFactory(
            queryProvider = queryProvider,
            countQuery = matchQueries.countMatches(id),
            transacter = matchQueries
        )

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(40)
            .setPageSize(20)
            .setPrefetchDistance(5)
            .build()

        val boundaryCallback = MatchBoundaryCallback(scope, service, matchQueries, id)

        val pagedList = LivePagedListBuilder(factory, config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return Response(
            pagedList = pagedList,
            status = boundaryCallback.status,
            retry = { boundaryCallback.retry() })
    }

    /**
     * Fetches the matches from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getMatchesLiveData] are notified.
     */
    suspend fun fetchMatches(id: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                val count = matchQueries.countMatches(id).executeAsOne()
                val list = if (count != 0L)
                // There are already some matches in the database
                // we want to refresh all of them
                    service.getMatches(id, count.toInt(), 0)
                else
                // There are no matches in the database,
                // we want to fetch only 20 from the network
                    service.getMatches(id, 20, 0)
                list.map {
                    it.accountId = id // response from the network doesn't contain any information
                    it // about who played this matches, so we need to set this manually
                }
                if (list.isNotEmpty()) {
                    matchQueries.transaction {
                        matchQueries.deleteAll(id)
                        list.forEach { matchQueries.insert(it.mapToDb()) }
                    }
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }

    fun fetchMatchesByHero(accountId: Long, heroId: Int): PagedResponse<MatchUI> {
        val sourceFactory = MatchesByHeroDataSourceFactory(accountId, heroId, service)
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
            retry = { sourceFactory.sourceLiveData.value?.retry() })
    }

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getMatchStatsLiveData(id: Long): LiveData<Stats> {
        return matchStatsDao.getMatchStats(id)
    }

    /**
     * Fetches the match-stats from the network and inserts it into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getMatchStatsLiveData] are notified.
     */
    suspend fun fetchMatchStats(matchId: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                val match = service.getMatch(matchId)
                db.runInTransaction {
                    matchStatsDao.insert(match)
                    playerStatsDao.insert(match.players ?: Collections.emptyList())
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }
}