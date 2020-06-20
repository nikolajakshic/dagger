package com.nikola.jakshic.dagger.profile.heroes

import com.nikola.jakshic.dagger.common.sqldelight.Heroes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeroJson(
    var accountId: Long = 0,
    @Json(name = "hero_id") var heroId: Long,
    @Json(name = "games") var gamesPlayed: Long,
    @Json(name = "win") var gamesWon: Long
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

fun Heroes.mapToUi(): HeroUI {
    return HeroUI(
        heroId = this.hero_id,
        gamesPlayed = this.games,
        gamesWon = this.wins
    )
}

fun mapToUi(
    account_id: Long,
    hero_id: Long,
    games: Long,
    wins: Long
): HeroUI {
    return HeroUI(
        heroId = hero_id,
        gamesPlayed = games,
        gamesWon = wins
    )
}