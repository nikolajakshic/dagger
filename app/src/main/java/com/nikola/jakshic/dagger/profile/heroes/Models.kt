package com.nikola.jakshic.dagger.profile.heroes

import com.nikola.jakshic.dagger.common.sqldelight.Heroes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeroJson(
    var accountId: Long = 0,
    @Json(name = "hero_id") val heroId: Long,
    @Json(name = "games") val gamesPlayed: Long,
    @Json(name = "win") val gamesWon: Long
)

data class HeroUI(
    val heroId: Long,
    val gamesPlayed: Long,
    val gamesWon: Long
)

fun HeroJson.mapToDb(): Heroes {
    return Heroes(
        account_id = this.accountId,
        hero_id = this.heroId,
        games = this.gamesPlayed,
        wins = this.gamesWon
    )
}

fun List<Heroes>.mapToUi(): List<HeroUI> {
    val list = mutableListOf<HeroUI>()
    for (item in this) {
        list.add(
            HeroUI(
                heroId = item.hero_id,
                gamesPlayed = item.games,
                gamesWon = item.wins
            )
        )
    }
    return list
}