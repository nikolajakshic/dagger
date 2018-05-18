package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.LiveData
import com.nikola.jakshic.dagger.data.local.HeroDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroRepository @Inject constructor(
        private val dao: HeroDao,
        private val service: OpenDotaService) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesLiveDataByGames(id: Long) = dao.getHeroesByGames(id)

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesLiveDataByWinrate(id: Long) = dao.getHeroesByWinrate(id)

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesLiveDataByWins(id: Long) = dao.getHeroesByWins(id)

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesLiveDataByLosses(id: Long) = dao.getHeroesByLosses(id)

    /**
     * Fetches the heroes from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getHeroesLiveDataByGames], [getHeroesLiveDataByWins],
     * [getHeroesLiveDataByLosses] and [getHeroesLiveDataByWinrate] are notified.
     *
     * @param onSuccess called on main thread
     * @param onError called on main thread
     */
    fun fetchHeroes(id: Long, onSuccess: () -> Unit, onError: () -> Unit): Disposable {
        return service.getHeroes(id)
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .map {
                    it.accountId = id   // response from the network doesn't contain any information
                    it           // about who played this heroes, so we need to set this manually
                }
                .toList()
                .flatMapCompletable { Completable.fromAction { dao.insertHeroes(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess() }, { onError() })
    }
}