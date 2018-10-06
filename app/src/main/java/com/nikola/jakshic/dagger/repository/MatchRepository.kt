package com.nikola.jakshic.dagger.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.Dispatcher.IO
import com.nikola.jakshic.dagger.data.local.DotaDatabase
import com.nikola.jakshic.dagger.data.local.MatchDao
import com.nikola.jakshic.dagger.data.local.MatchStatsDao
import com.nikola.jakshic.dagger.data.local.PlayerStatsDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Match
import com.nikola.jakshic.dagger.vo.Stats
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
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
    fun getMatchesLiveData(
            job: Job,
            id: Long,
            onLoading: () -> Unit,
            onSuccess: () -> Unit,
            onError: () -> Unit): LiveData<PagedList<Match>> {

        val factory = matchDao.getMatches(id)
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(40)
                .setPageSize(20)
                .setPrefetchDistance(5)
                .build()
        return LivePagedListBuilder(factory, config)
                .setBoundaryCallback(
                        MatchBoundaryCallback(job, service, matchDao, id, onLoading, onSuccess, onError))
                .build()
    }

    /**
     * Fetches the matches from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getMatchesLiveData] are notified.
     *
     * @param onSuccess called on main thread
     * @param onError called on main thread
     */
    fun fetchMatches(job: Job, id: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        launch(UI, parent = job) {
            try {
                withContext(IO) {
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
    fun fetchMatchStats(job: Job, matchId: Long) {
        launch(UI, parent = job) {
            try {
                withContext(IO) {
                    val match = service.getMatch(matchId).await()
                    db.runInTransaction {
                        matchStatsDao.insert(match)
                        playerStatsDao.insert(match.players ?: Collections.emptyList())
                    }
                }
            } catch (e: Exception) {

            }
        }
    }
}