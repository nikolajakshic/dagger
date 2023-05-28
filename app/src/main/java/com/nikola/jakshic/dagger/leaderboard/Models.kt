package com.nikola.jakshic.dagger.leaderboard

import com.nikola.jakshic.dagger.common.sqldelight.leaderboard.SelectAll
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class _Leaderboard(@Json(name = "leaderboard") val leaderboard: List<LeaderboardJson>?)

@JsonClass(generateAdapter = true)
data class LeaderboardJson(
    @Json(name = "name") val name: String?
)

@JsonClass(generateAdapter = true)
data class RemoteConfig(
    @Json(name = "leaderboard_url") val leaderboardUrl: String,
    @Json(name = "items_version") val itemsVersion: Long,
    @Json(name = "items_url") val itemsUrl: String,
    @Json(name = "heroes_version") val heroesVersion: Long,
    @Json(name = "heroes_url") val heroesUrl: String
)

enum class Region {
    AMERICAS,
    CHINA,
    EUROPE,
    SE_ASIA
}

data class LeaderboardUI(
    val rank: Long,
    val name: String?
)

fun List<SelectAll>.mapToUi(): List<LeaderboardUI> {
    val list = mutableListOf<LeaderboardUI>()
    for ((index, item) in this.withIndex()) {
        list.add(LeaderboardUI(index + 1L, item.name))
    }
    return list
}
