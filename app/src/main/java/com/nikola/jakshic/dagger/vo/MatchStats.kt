package com.nikola.jakshic.dagger.vo

import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "match_stats")
@JsonClass(generateAdapter = true)
class MatchStats {
    @PrimaryKey @ColumnInfo(name = "match_id") @Json(name = "match_id") var matchId: Long = 0
    @ColumnInfo(name = "radiant_win") @Json(name = "radiant_win") var isRadiantWin = false
    @ColumnInfo(name = "dire_score") @Json(name = "dire_score") var direScore: Int = 0
    @ColumnInfo(name = "radiant_score") @Json(name = "radiant_score") var radiantScore: Int = 0
    @ColumnInfo(name = "skill") @Json(name = "skill") var skill: Int = 0
    @ColumnInfo(name = "game_mode") @Json(name = "game_mode") var mode: Int = 0
    @ColumnInfo(name = "duration") @Json(name = "duration") var duration: Int = 0
    @ColumnInfo(name = "start_time") @Json(name = "start_time") var startTime: Long = 0
    @ColumnInfo(name = "radiant_barracks") @Json(name = "barracks_status_radiant") var radiantBarracks: Int = 0
    @ColumnInfo(name = "dire_barracks") @Json(name = "barracks_status_dire") var direBarracks: Int = 0
    @ColumnInfo(name = "radiant_towers") @Json(name = "tower_status_radiant") var radiantTowers: Int = 0
    @ColumnInfo(name = "dire_towers") @Json(name = "tower_status_dire") var direTowers: Int = 0
    @Embedded(prefix = "radiant_") @Json(name = "radiant_team") var radiantTeam: Team? = null
    @Embedded(prefix = "dire_") @Json(name = "dire_team") var direTeam: Team? = null
    @Ignore @Json(name = "players") var players: List<PlayerStats>? = null
}

@JsonClass(generateAdapter = true)
data class Team(@Json(name = "name") var name: String?)

// POJO for Database Query
class Stats {
    @Embedded
    var matchStats: MatchStats? = null
    @Relation(
            entity = PlayerStats::class,
            entityColumn = "match_id",
            parentColumn = "match_id")
    var playerStats: List<PlayerStats>? = null
}