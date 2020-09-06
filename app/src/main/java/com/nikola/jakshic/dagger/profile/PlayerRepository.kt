package com.nikola.jakshic.dagger.profile

import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.PlayerQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
    private val service: OpenDotaService,
    private val playerQueries: PlayerQueries
) {

    suspend fun getProfile(id: Long) = coroutineScope {
        val profileDef = async(Dispatchers.IO) { service.getPlayerProfile(id) }
        val winsLossesDef = async(Dispatchers.IO) { service.getPlayerWinLoss(id) }

        val profile = profileDef.await()
        val winsLosses = winsLossesDef.await()

        withContext(Dispatchers.IO) {
            playerQueries.upsert(
                name = profile.player!!.name,
                personaName = profile.player.personaName,
                avatarUrl = profile.player.avatarUrl,
                rankTier = profile.rankTier,
                leaderboardRank = profile.leaderboardRank,
                wins = winsLosses.wins,
                losses = winsLosses.losses,
                accountId = profile.player.id
            )
        }
    }

    suspend fun fetchPlayers(name: String, onSuccess: (List<PlayerUI>) -> Unit, onError: () -> Unit) {
        try {
            val list = withContext(Dispatchers.IO) {
                service.searchPlayers(name)
                    .map(PlayerJson::mapToUi)
            }
            onSuccess(list)
        } catch (e: Exception) {
            onError()
        }
    }
}