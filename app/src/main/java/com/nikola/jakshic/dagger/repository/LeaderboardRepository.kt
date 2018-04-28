package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.data.local.LeaderboardDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepository @Inject constructor(
        private val dao: LeaderboardDao,
        private val service: OpenDotaService) {

    /**
     * Fetches the data from the network, and takes the first 100 players
     */
    fun fetchData(region: String, status: MutableLiveData<Status>): Disposable {
        status.value = Status.LOADING
        return service.getLeaderboard(region)
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it.leaderboard) }
                .take(100)
                .map {
                    it.region = region // json response doesn't contain info about region
                    it             // we need to set it manually, so we can query the database
                }                        // by regions
                .toList()
                .subscribe({ list ->
                    dao.deleteLeaderboards(region)  // delete the old data
                    dao.insertLeaderboard(list)     // insert the new one
                    status.postValue(Status.SUCCESS)
                }, { error ->
                    status.postValue(Status.ERROR)
                })
    }
}