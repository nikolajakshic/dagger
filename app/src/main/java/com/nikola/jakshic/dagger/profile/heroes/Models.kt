package com.nikola.jakshic.dagger.profile.heroes

import com.nikola.jakshic.dagger.common.sqldelight.Heroes
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllByGames
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllByLosses
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllByWinrate
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllByWins
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeroJson(
    @Json(name = "hero_id") val heroId: Long,
    @Json(name = "games") val gamesPlayed: Long,
    @Json(name = "win") val gamesWon: Long
)

data class HeroUI(
    val heroId: Long,
    val imagePath: String?,
    val gamesPlayed: Long,
    val gamesWon: Long
)

fun HeroJson.mapToDb(accountId: Long): Heroes {
    return Heroes(
        account_id = accountId,
        hero_id = this.heroId,
        games = this.gamesPlayed,
        wins = this.gamesWon
    )
}

fun SelectAllByGames.mapToUi(): HeroUI {
    return HeroUI(
        heroId = hero_id,
        imagePath = image_path,
        gamesPlayed = games,
        gamesWon = wins
    )
}

fun SelectAllByWinrate.mapToUi(): HeroUI {
    return HeroUI(
        heroId = hero_id,
        imagePath = image_path,
        gamesPlayed = games,
        gamesWon = wins
    )
}

fun SelectAllByWins.mapToUi(): HeroUI {
    return HeroUI(
        heroId = hero_id,
        imagePath = image_path,
        gamesPlayed = games,
        gamesWon = wins
    )
}

fun SelectAllByLosses.mapToUi(): HeroUI {
    return HeroUI(
        heroId = hero_id,
        imagePath = image_path,
        gamesPlayed = games,
        gamesWon = wins
    )
}
