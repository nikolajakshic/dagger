package com.nikola.jakshic.dagger.leaderboard

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class _Leaderboard(@Json(name = "leaderboard") val leaderboard: List<LeaderboardJson>?)

@JsonClass(generateAdapter = true)
data class LeaderboardJson(
    var id: Int = 0,
    @Json(name = "name") var name: String?,
    var region: String?
)

enum class Region {
    AMERICAS,
    CHINA,
    EUROPE,
    SE_ASIA
}