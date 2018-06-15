package com.nikola.jakshic.dagger.repository

import com.nikola.jakshic.dagger.data.local.PlayerDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Player
import com.nikola.jakshic.dagger.vo._Player
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
        private val service: OpenDotaService,
        private val dao: PlayerDao) {

    fun getProfile(id: Long, onSuccess: () -> Unit, onError: () -> Unit): Disposable {
        val profile = service.getPlayerProfile(id)
        val winsLosses = service.getPlayerWinLoss(id)

        return Observable.zip(profile, winsLosses,
                BiFunction { t1: _Player, t2: Player ->
                    t1.player!!.rankTier = t1.rankTier
                    t1.player.leaderboardRank = t1.leaderboardRank
                    t1.player.wins = t2.wins
                    t1.player.losses = t2.losses
                    t1
                })
                .subscribeOn(Schedulers.io())
                .flatMapCompletable { Completable.fromAction { dao.insertPlayer(it.player!!) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess() }, { onError() })
    }

    fun fetchPlayers(job: Job, name: String, onSuccess: (List<Player>) -> Unit, onError: () -> Unit) {
        launch(UI, parent = job) {
            try {
                val list = service.searchPlayers(name).await()
                onSuccess(list)
            } catch (e: Exception) {
                onError()
            }
        }
    }
}