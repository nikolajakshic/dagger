package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.LiveData
import com.nikola.jakshic.dagger.Dispatcher.IO
import com.nikola.jakshic.dagger.data.local.LeaderboardDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Leaderboard
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepository @Inject constructor(
        private val dao: LeaderboardDao,
        private val service: OpenDotaService) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getLeaderboardLiveData(region: String): LiveData<List<Leaderboard>> {
        return dao.getLeaderboard(region)
    }

    /**
     * Fetches the leaderboard from the network, takes the first 100 players
     * and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getLeaderboardLiveData] are notified.
     *
     * @param onSuccess called on main thread
     * @param onError called on main thread
     */
    fun fetchLeaderboard(job: Job, region: String, onSuccess: () -> Unit, onError: () -> Unit) {
        launch(UI, parent = job) {
            try {
                val leaderboard = service.getLeaderboard(region).await().leaderboard ?: throw Exception()
                withContext(IO) {
                    val list = leaderboard.take(100)
                    list.map {
                        it.region = region  // response from the network doesn't contain any information
                        it            // about the region, so we need to set this manually
                    }
                    if (list.isNotEmpty()) {
                        // We don't have players ids, we only have their names,
                        // if the player has changed his name in the meantime,
                        // that would result into 2 rows in the database for a single player.
                        // So we need to remove all players from the database and then insert
                        // the fresh ones.
                        dao.deleteLeaderboards(region)
                        dao.insertLeaderboard(list)
                    }
                }
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }
}