package com.nikola.jakshic.dagger.profile

import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.PlayerQueries
import com.nikola.jakshic.dagger.common.sqldelight.Players
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
    private val sqlDriver: SqlDriver,
    private val service: OpenDotaService,
    private val playerQueries: PlayerQueries
) {

    suspend fun getProfile(id: Long) = coroutineScope {
        val profileDef = async(Dispatchers.IO) { service.getPlayerProfile(id) }
        val winsLossesDef = async(Dispatchers.IO) { service.getPlayerWinLoss(id) }

        val profile = profileDef.await()
        val winsLosses = winsLossesDef.await()

        withContext(Dispatchers.IO) {
            playerQueries.transaction {
                // TODO THIS IS TEMPORARY - DO THIS IN GROUPED QUERY
                sqlDriver.execute(0, "PRAGMA foreign_keys = OFF;", 0)
                playerQueries.insert(
                    Players(
                        account_id = profile.player!!.id,
                        name = profile.player.name,
                        persona_name = profile.player.personaName,
                        avatar_url = profile.player.avatarUrl,
                        rank_tier = profile.rankTier,
                        leaderboard_rank = profile.leaderboardRank,
                        wins = winsLosses.wins,
                        losses = winsLosses.losses
                    )
                )
                sqlDriver.execute(0, "PRAGMA foreign_keys = ON;", 0)
            }
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