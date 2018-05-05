package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.data.local.HeroDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroRepository @Inject constructor(
        private val dao: HeroDao,
        private val service: OpenDotaService) {

    fun fetchHeroes(status: MutableLiveData<Status>, id: Long): Disposable {
        status.value = Status.LOADING

        return service.getHeroes(id)
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .map {
                    it.accountId = id
                    it
                }
                .toList()
                .subscribe({
                    dao.insertHeroes(it)
                    status.postValue(Status.SUCCESS)
                }, {
                    status.postValue(Status.ERROR)
                })
    }

    fun sortByGames(id: Long) = dao.getHeroesByGames(id)

    fun sortByWinRate(id: Long) = dao.getHeroesByWinrate(id)

    fun sortByWins(id: Long) = dao.getHeroesByWins(id)

    fun sortByLosses(id: Long) = dao.getHeroesByLosses(id)
}