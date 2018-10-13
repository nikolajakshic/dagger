package com.nikola.jakshic.dagger.repository

import com.nikola.jakshic.dagger.data.local.PlayerDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Player
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.coroutineScope
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
        private val service: OpenDotaService,
        private val dao: PlayerDao) {

    suspend fun getProfile(id: Long, onSuccess: () -> Unit, onError: () -> Unit) = coroutineScope {
        try {
            withContext(Dispatchers.IO) {
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

    suspend fun fetchPlayers(name: String, onSuccess: (List<Player>) -> Unit, onError: () -> Unit) {
        coroutineScope {
            try {
                val list = service.searchPlayers(name).await()
                onSuccess(list)
            } catch (e: Exception) {
                onError()
            }
        }
    }
}