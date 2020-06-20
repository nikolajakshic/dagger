package com.nikola.jakshic.dagger.profile

import com.nikola.jakshic.dagger.common.sqldelight.Players
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class _Player(
    @Json(name = "profile") val player: PlayerJson?,
    @Json(name = "rank_tier") val rankTier: Long,
    @Json(name = "leaderboard_rank") val leaderboardRank: Long
)

@JsonClass(generateAdapter = true)
data class PlayerJson(
    @Json(name = "account_id") var id: Long = 0,
    @Json(name = "name") var name: String?,
    @Json(name = "personaname") var personaName: String?,
    @Json(name = "avatarfull") var avatarUrl: String?,
    @Json(name = "rank_tier") var rankTier: Long = 0,
    @Json(name = "leaderboard_rank") var leaderboardRank: Long = 0,
    @Json(name = "win") var wins: Long = 0,
    @Json(name = "lose") var losses: Long = 0
)

data class PlayerUI(
    val id: Long = 0,
    val name: String?,
    val personaName: String?,
    val avatarUrl: String?,
    val rankTier: Long = 0,
    val leaderboardRank: Long = 0,
    val wins: Long = 0,
    val losses: Long = 0
)

fun Players.mapToUi(): PlayerUI {
    return PlayerUI(
        account_id,
        name,
        persona_name,
        avatar_url,
        rank_tier,
        leaderboard_rank,
        wins,
        losses
    )
}

fun PlayerJson.mapToUi(): PlayerUI {
    return PlayerUI(
        id = id,
        name = name,
        personaName = personaName,
        avatarUrl = avatarUrl,
        rankTier = rankTier,
        leaderboardRank = leaderboardRank,
        wins = wins,
        losses = losses
    )
}