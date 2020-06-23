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

enum class Region {
    AMERICAS,
    CHINA,
    EUROPE,
    SE_ASIA
}

data class LeaderboardUI(
    val name: String?
)

fun List<SelectAll>.mapToUi(): List<LeaderboardUI> {
    val list = mutableListOf<LeaderboardUI>()
    for (item in this) {
        list.add(LeaderboardUI(item.name))
    }
    return list
}