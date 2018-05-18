package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.LiveData
import com.nikola.jakshic.dagger.data.local.PeerDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeerRepository @Inject constructor(
        private val service: OpenDotaService,
        private val dao: PeerDao) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getPeersLiveDataByGames(id: Long) = dao.getByGames(id)

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getPeersLiveDataByWinrate(id: Long) = dao.getByWinrate(id)

    /**
     * Fetches the peers from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getPeersLiveDataByGames] and [getPeersLiveDataByWinrate] are notified.
     *
     * @param onSuccess called on main thread
     * @param onError called on main thread
     */
    fun fetchPeers(id: Long, onSuccess: () -> Unit, onError: () -> Unit): Disposable {
        return service.getPeers(id)
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .filter { it.withGames != 0 } // filter opponents from the peer list
                .map {
                    it.accountId = id   // response from the network doesn't contain any information
                    it            // about whose this peers are, so we need to set this manually
                }
                .toList()
                .flatMapCompletable { Completable.fromAction { dao.insertPeers(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess() }, { onError() })
    }
}