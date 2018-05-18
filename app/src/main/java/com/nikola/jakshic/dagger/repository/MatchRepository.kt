package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.nikola.jakshic.dagger.data.local.DotaDatabase
import com.nikola.jakshic.dagger.data.local.MatchDao
import com.nikola.jakshic.dagger.data.local.MatchStatsDao
import com.nikola.jakshic.dagger.data.local.PlayerStatsDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Match
import com.nikola.jakshic.dagger.vo.Stats
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
            id: Long,
            disposables: CompositeDisposable,
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
                        MatchBoundaryCallback(service, matchDao, disposables, id, onLoading, onSuccess, onError))
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
    fun fetchMatches(id: Long, onSuccess: () -> Unit, onError: () -> Unit): Disposable {
        return matchDao.getMatchCount(id)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMap { limit ->
                    if (limit != 0)
                    // There are already some matches in the database
                    // we want to refresh all of them
                        service.getMatches(id, limit, 0)
                    else
                    // There are no matches in the database,
                    // we want to fetch only 20 from the network
                        service.getMatches(id, 20, 0)
                }
                .flatMap { Observable.fromIterable(it) }
                .map {
                    it.accountId = id   // response from the network doesn't contain any information
                    it           // about who played this matches, so we need to set this manually
                }
                .toList()
                .flatMapCompletable {
                    Completable.fromAction {
                        if (it.size != 0) {
                            matchDao.deleteMatches(id)
                            matchDao.insertMatches(it)
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess() }, { onError() })
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
    fun fetchMatchStats(matchId: Long): Disposable {
        return service.getMatch(matchId)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable {
                    Completable.fromAction {
                        db.runInTransaction {
                            matchStatsDao.insert(it)
                            playerStatsDao.insert(it.players ?: Collections.emptyList())
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {})
    }
}