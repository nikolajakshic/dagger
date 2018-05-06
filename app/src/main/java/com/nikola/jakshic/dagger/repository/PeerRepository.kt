package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.data.local.PeerDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeerRepository @Inject constructor(
        private val service: OpenDotaService,
        private val dao: PeerDao) {

    fun fetchPeers(status: MutableLiveData<Status>, id: Long): Disposable {
        status.value = Status.LOADING

        return service.getPeers(id)
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .filter { it.withGames != 0 } // filter opponents from peer list
                .map {
                    it.accountId = id
                    it
                }
                .toList()
                .subscribe({
                    dao.insertPeers(it)
                    status.postValue(Status.SUCCESS)
                },{
                    status.postValue(Status.ERROR)
                })
    }

    fun sortByGames(id: Long) = dao.getByGames(id)

    fun sortByWinRate(id: Long) = dao.getByWinrate(id)
}