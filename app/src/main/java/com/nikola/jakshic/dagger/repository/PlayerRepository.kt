package com.nikola.jakshic.dagger.repository

import com.nikola.jakshic.dagger.data.local.PlayerDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
        private val service: OpenDotaService,
        private val dao: PlayerDao) {

    suspend fun getProfile(id: Long) = coroutineScope {
        val profileDef = async(Dispatchers.IO) { service.getPlayerProfile(id) }
        val winsLossesDef = async(Dispatchers.IO) { service.getPlayerWinLoss(id) }

        val profile = profileDef.await()
        val winsLosses = winsLossesDef.await()

        profile.player!!.rankTier = profile.rankTier
        profile.player.leaderboardRank = profile.leaderboardRank
        profile.player.wins = winsLosses.wins
        profile.player.losses = winsLosses.losses

        withContext(Dispatchers.IO) { dao.insertPlayer(profile.player) }
    }

    suspend fun fetchPlayers(name: String, onSuccess: (List<Player>) -> Unit, onError: () -> Unit) {
        try {
            val list = withContext(Dispatchers.IO) { service.searchPlayers(name) }
            onSuccess(list)
        } catch (e: Exception) {
            onError()
        }
    }
}