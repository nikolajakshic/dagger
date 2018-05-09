package com.nikola.jakshic.dagger.vo

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "match_stats")
class MatchStats {
    @PrimaryKey @ColumnInfo(name = "match_id") @SerializedName("match_id") @Expose var matchId: Long = 0
    @ColumnInfo(name = "radiant_win") @SerializedName("radiant_win") @Expose var isRadiantWin = false
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