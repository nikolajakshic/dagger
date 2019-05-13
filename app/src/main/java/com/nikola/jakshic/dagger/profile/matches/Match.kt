package com.nikola.jakshic.dagger.profile.matches

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "matches", primaryKeys = arrayOf("match_id", "account_id"))
@JsonClass(generateAdapter = true)
data class Match(
    @ColumnInfo(name = "account_id") var accountId: Long = 0,
    @ColumnInfo(name = "match_id") @Json(name = "match_id") var matchId: Long,
    @ColumnInfo(name = "hero_id") @Json(name = "hero_id") var heroId: Int,
    @ColumnInfo(name = "player_slot") @Json(name = "player_slot") var playerSlot: Int,
    @ColumnInfo(name = "skill") @Json(name = "skill") var skill: Int,
    @ColumnInfo(name = "duration") @Json(name = "duration") var duration: Int,
    @ColumnInfo(name = "mode") @Json(name = "game_mode") var gameMode: Int,
    @ColumnInfo(name = "lobby") @Json(name = "lobby_type") var lobbyType: Int,
    @ColumnInfo(name = "radiant_win") @Json(name = "radiant_win") var isRadiantWin: Boolean,
    @ColumnInfo(name = "start_time") @Json(name = "start_time") var startTime: Long
)