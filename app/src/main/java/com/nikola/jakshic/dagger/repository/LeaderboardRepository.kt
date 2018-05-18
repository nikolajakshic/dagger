package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.LiveData
import com.nikola.jakshic.dagger.data.local.LeaderboardDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Leaderboard
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
    fun fetchLeaderboard(region: String, onSuccess: () -> Unit, onError: () -> Unit): Disposable {
        return service.getLeaderboard(region)
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it.leaderboard) }
                .take(100)
                .map {
                    it.region = region  // response from the network doesn't contain any information
                    it            // about the region, so we need to set this manually
                }
                .toList()
                .flatMapCompletable {
                    Completable.fromAction {
                        if (it.size != 0) {
                            // We don't have players ids, we only have their names,
                            // if the player has changed his name in the meantime,
                            // that would result into 2 rows in the database for a single player.
                            // So we need to remove all players from the database and then insert
                            // the fresh ones.
                            dao.deleteLeaderboards(region)
                            dao.insertLeaderboard(it)
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess() }, { onError() })
    }
}