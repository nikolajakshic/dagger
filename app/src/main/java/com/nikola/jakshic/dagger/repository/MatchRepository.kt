package com.nikola.jakshic.dagger.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.data.local.DotaDatabase
import com.nikola.jakshic.dagger.data.local.MatchDao
import com.nikola.jakshic.dagger.data.local.MatchStatsDao
import com.nikola.jakshic.dagger.data.local.PlayerStatsDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.ui.profile.matches.Response
import com.nikola.jakshic.dagger.ui.profile.matches.byhero.MatchesByHeroDataSourceFactory
import com.nikola.jakshic.dagger.ui.profile.matches.byhero.PagedResponse
import com.nikola.jakshic.dagger.vo.Match
import com.nikola.jakshic.dagger.vo.Stats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
        private val service: OpenDotaService,
        private val db: DotaDatabase,
        private val matchDao: MatchDao,
        private val matchStatsDao: MatchStatsDao,
        private val playerStatsDao: PlayerStatsDao) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getMatchesLiveData(scope: CoroutineScope, id: Long): Response {

        val factory = matchDao.getMatches(id)
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(40)
                .setPageSize(20)
                .setPrefetchDistance(5)
                .build()

        val boundaryCallback = MatchBoundaryCallback(scope, service, matchDao, id)

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
                val count = matchDao.getMatchCount(id)
                val list = if (count != 0)
                // There are already some matches in the database
                // we want to refresh all of them
                    service.getMatches(id, count, 0).await()
                else
                // There are no matches in the database,
                // we want to fetch only 20 from the network
                    service.getMatches(id, 20, 0).await()
                list.map {
                    it.accountId = id   // response from the network doesn't contain any information
                    it           // about who played this matches, so we need to set this manually
                }
                if (list.isNotEmpty()) {
                    matchDao.deleteMatches(id)
                    matchDao.insertMatches(list)
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }

    fun fetchMatchesByHero(accountId: Long, heroId: Int): PagedResponse<Match> {
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
                val match = service.getMatch(matchId).await()
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