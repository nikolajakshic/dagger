package com.nikola.jakshic.dagger.leaderboard

import com.nikola.jakshic.dagger.common.sqldelight.SelectAll
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class _Leaderboard(@Json(name = "leaderboard") val leaderboard: List<LeaderboardJson>?)

@JsonClass(generateAdapter = true)
data class LeaderboardJson(
    @Json(name = "name") val name: String?
)

@JsonClass(generateAdapter = true)
data class LeaderboardUrlJson(@Json(name = "leaderboard_url") val url: String)

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
