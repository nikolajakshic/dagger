package com.nikola.jakshic.dagger.profile.heroes

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.nikola.jakshic.dagger.common.sqldelight.Heroes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "heroes", primaryKeys = arrayOf("account_id", "hero_id"))
@JsonClass(generateAdapter = true)
data class HeroJson(
    @ColumnInfo(name = "account_id") var accountId: Long = 0,
    @ColumnInfo(name = "hero_id") @Json(name = "hero_id") var heroId: Int,
    @ColumnInfo(name = "games") @Json(name = "games") var gamesPlayed: Int,
    @ColumnInfo(name = "wins") @Json(name = "win") var gamesWon: Int
)

data class HeroUI(
    val heroId: Int,
    val gamesPlayed: Int,
    val gamesWon: Int
)

fun HeroJson.mapToDb(): Heroes {
    return Heroes(
        account_id = this.accountId,
        hero_id = this.heroId.toLong(),
        games = this.gamesPlayed.toLong(),
        wins = this.gamesWon.toLong()
    )
}

fun Heroes.mapToUi(): HeroUI {
    return HeroUI(
        heroId = this.hero_id.toInt(),
        gamesPlayed = this.games.toInt(),
        gamesWon = this.wins.toInt()
    )
}

fun mapToUi(
    account_id: Long,
    hero_id: Long,
    games: Long,
    wins: Long
): HeroUI {
    return HeroUI(
        heroId = hero_id.toInt(),
        gamesPlayed = games.toInt(),
        gamesWon = wins.toInt()
    )
}