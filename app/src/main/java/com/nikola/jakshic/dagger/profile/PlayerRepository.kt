package com.nikola.jakshic.dagger.profile

import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.PlayerQueries
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
    private val service: OpenDotaService,
    private val playerQueries: PlayerQueries,
    private val dispatchers: Dispatchers,
) {
    suspend fun fetchProfile(id: Long) = coroutineScope {
        val profileDef = async(dispatchers.io) { service.getPlayerProfile(id) }
        val winsLossesDef = async(dispatchers.io) { service.getPlayerWinLoss(id) }

        val profile = profileDef.await()
        val winsLosses = winsLossesDef.await()

        withContext(dispatchers.io) {
            playerQueries.upsert(
                name = profile.player!!.name,
                personaName = profile.player.personaName,
                avatarUrl = profile.player.avatarUrl,
                rankTier = profile.rankTier,
                leaderboardRank = profile.leaderboardRank,
                wins = winsLosses.wins,
                losses = winsLosses.losses,
                accountId = profile.player.id,
            )
        }
    }

    suspend fun fetchPlayers(name: String): List<PlayerUI> = withContext(dispatchers.io) {
        return@withContext service.searchPlayers(name)
            .map(PlayerJson::mapToUi)
    }
}
