package com.nikola.jakshic.dagger.profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class _Player(
    @Json(name = "profile") val player: PlayerJson?,
    @Json(name = "rank_tier") val rankTier: Int,
    @Json(name = "leaderboard_rank") val leaderboardRank: Int
)

@Entity(tableName = "players")
@JsonClass(generateAdapter = true)
data class PlayerJson(
    @PrimaryKey @ColumnInfo(name = "account_id") @Json(name = "account_id") var id: Long = 0,
    @ColumnInfo(name = "name") @Json(name = "name") var name: String?,
    @ColumnInfo(name = "persona_name") @Json(name = "personaname") var personaName: String?,
    @ColumnInfo(name = "avatar_url") @Json(name = "avatarfull") var avatarUrl: String?,
    @ColumnInfo(name = "rank_tier") @Json(name = "rank_tier") var rankTier: Int = 0,
    @ColumnInfo(name = "leaderboard_rank") @Json(name = "leaderboard_rank") var leaderboardRank: Int = 0,
    @ColumnInfo(name = "wins") @Json(name = "win") var wins: Int = 0,
    @ColumnInfo(name = "losses") @Json(name = "lose") var losses: Int = 0
)