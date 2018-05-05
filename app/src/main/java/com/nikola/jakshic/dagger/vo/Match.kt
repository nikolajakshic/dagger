package com.nikola.jakshic.dagger.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "matches", primaryKeys = arrayOf("match_id", "account_id"))
data class Match(
        @ColumnInfo(name = "account_id") var accountId: Long,
        @ColumnInfo(name = "match_id") @SerializedName("match_id") @Expose var matchId: Long,
        @ColumnInfo(name = "hero_id") @SerializedName("hero_id") @Expose  var heroId: Long,
        @ColumnInfo(name = "player_slot") @SerializedName("player_slot") @Expose  var playerSlot: Long,
        @ColumnInfo(name = "skill") @SerializedName("skill") @Expose  var skill: Long,
        @ColumnInfo(name = "duration") @SerializedName("duration") @Expose var duration: Long,
        @ColumnInfo(name = "mode") @SerializedName("game_mode") @Expose var gameMode: Long,
        @ColumnInfo(name = "lobby") @SerializedName("lobby_type") @Expose var lobbyType: Long,
        @ColumnInfo(name = "radiant_win") @SerializedName("radiant_win") @Expose var isRadiantWin: Boolean,
        @ColumnInfo(name = "start_time") @SerializedName("start_time") @Expose var startTime: Long
)