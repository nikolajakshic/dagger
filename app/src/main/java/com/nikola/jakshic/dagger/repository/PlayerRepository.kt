package com.nikola.jakshic.dagger.repository

import com.nikola.jakshic.dagger.Dispatcher.IO
import com.nikola.jakshic.dagger.data.local.PlayerDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Player
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
        private val service: OpenDotaService,
        private val dao: PlayerDao) {

    fun getProfile(job: Job, id: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        launch(UI, parent = job) {
            try {
                withContext(IO) {
                    val profileDef = service.getPlayerProfile(id)
                    val winsLossesDef = service.getPlayerWinLoss(id)

                    val profile = profileDef.await()
                    val winsLosses = winsLossesDef.await()

                    profile.player!!.rankTier = profile.rankTier
                    profile.player.leaderboardRank = profile.leaderboardRank
                    profile.player.wins = winsLosses.wins
                    profile.player.losses = winsLosses.losses

                    dao.insertPlayer(profile.player)
                }
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
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