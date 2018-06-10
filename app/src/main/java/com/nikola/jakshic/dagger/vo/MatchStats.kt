package com.nikola.jakshic.dagger.vo

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "match_stats")
class MatchStats {
    @PrimaryKey @ColumnInfo(name = "match_id") @SerializedName("match_id") @Expose var matchId: Long = 0
    @ColumnInfo(name = "radiant_win") @SerializedName("radiant_win") @Expose var isRadiantWin = false
    @ColumnInfo(name = "dire_score") @SerializedName("dire_score") @Expose var direScore: Int = 0
    @ColumnInfo(name = "radiant_score") @SerializedName("radiant_score") @Expose var radiantScore: Int = 0
    @ColumnInfo(name = "skill") @SerializedName("skill") @Expose var skill: Int = 0
    @ColumnInfo(name = "game_mode") @SerializedName("game_mode") @Expose var mode: Int = 0
    @ColumnInfo(name = "duration") @SerializedName("duration") @Expose var duration: Int = 0
    @ColumnInfo(name = "start_time") @SerializedName("start_time") @Expose var startTime: Long = 0
    @Embedded(prefix = "radiant_") @SerializedName("radiant_team") @Expose var radiantTeam: Team? = null
    @Embedded(prefix = "dire_") @SerializedName("dire_team") @Expose var direTeam: Team? = null
    @Ignore @SerializedName("players") @Expose var players: List<PlayerStats>? = null
}

data class Team(@SerializedName("name") @Expose var name: String?)

// POJO for Database Query
class Stats{
    @Embedded var matchStats: MatchStats? = null
    @Relation(
            entity = PlayerStats::class,
            entityColumn = "match_id",
            parentColumn = "match_id")
    var playerStats : List<PlayerStats>? = null
}