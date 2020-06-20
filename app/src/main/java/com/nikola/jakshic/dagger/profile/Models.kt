package com.nikola.jakshic.dagger.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class _Player(
    @Json(name = "profile") val player: PlayerJson?,
    @Json(name = "rank_tier") val rankTier: Int,
    @Json(name = "leaderboard_rank") val leaderboardRank: Int
)

@JsonClass(generateAdapter = true)
data class PlayerJson(
    @Json(name = "account_id") var id: Long = 0,
    @Json(name = "name") var name: String?,
    @Json(name = "personaname") var personaName: String?,
    @Json(name = "avatarfull") var avatarUrl: String?,
    @Json(name = "rank_tier") var rankTier: Int = 0,
    @Json(name = "leaderboard_rank") var leaderboardRank: Int = 0,
    @Json(name = "win") var wins: Int = 0,
    @Json(name = "lose") var losses: Int = 0
)