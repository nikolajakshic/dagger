package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.data.local.PlayerDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.model.Player
import com.nikola.jakshic.dagger.model._Player
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
        private val service: OpenDotaService,
        private val dao: PlayerDao) {

    fun getProfile(id: Long): Disposable {
        val profile = service.getPlayerProfile(id)
        val winsLosses = service.getPlayerWinLoss(id)

        return Observable.zip(profile, winsLosses, BiFunction { t1: _Player, t2: Player ->
            t1.player.rankTier = t1.rankTier
            t1.player.leaderboardRank = t1.leaderboardRank
            t1.player.wins = t2.wins
            t1.player.losses = t2.losses
            t1
        })
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { dao.insertPlayer(it.player) },
                        { error -> })
    }

    fun fetchPlayers(
            disposables: CompositeDisposable,
            list: MutableLiveData<List<Player>>,
            status: MutableLiveData<Status>,
            name: String): Disposable {

        // Users can hit the search button multiple times
        // So we need to cancel previous call
        disposables.clear()

        status.value = Status.LOADING
        return service.searchPlayers(name)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    list.postValue(it)
                    status.postValue(Status.SUCCESS)
                }, {
                    status.postValue(Status.ERROR)
                })
    }
}