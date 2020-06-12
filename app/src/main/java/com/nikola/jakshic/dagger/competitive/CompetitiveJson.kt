package com.nikola.jakshic.dagger.competitive

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "competitive")
@JsonClass(generateAdapter = true)
data class CompetitiveJson(
    @PrimaryKey @ColumnInfo(name = "match_id") @Json(name = "match_id") var matchId: Long,
    @ColumnInfo(name = "start_time") @Json(name = "start_time") var startTime: Long,
    @ColumnInfo(name = "duration") @Json(name = "duration") var duration: Int,
    @ColumnInfo(name = "radiant_name") @Json(name = "radiant_name") var radiantName: String?,
    @ColumnInfo(name = "dire_name") @Json(name = "dire_name") var direName: String?,
    @ColumnInfo(name = "league_name") @Json(name = "league_name") var leagueName: String?,
    @ColumnInfo(name = "radiant_score") @Json(name = "radiant_score") var radiantScore: Int,
    @ColumnInfo(name = "dire_score") @Json(name = "dire_score") var direScore: Int,
    @ColumnInfo(name = "radiant_win") @Json(name = "radiant_win") var isRadiantWin: Boolean
)