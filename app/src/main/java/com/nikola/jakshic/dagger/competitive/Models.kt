package com.nikola.jakshic.dagger.competitive

import com.nikola.jakshic.dagger.common.sqldelight.Competitive
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitiveJson(
    @Json(name = "match_id") var matchId: Long,
    @Json(name = "start_time") var startTime: Long,
    @Json(name = "duration") var duration: Int,
    @Json(name = "radiant_name") var radiantName: String?,
    @Json(name = "dire_name") var direName: String?,
    @Json(name = "league_name") var leagueName: String?,
    @Json(name = "radiant_score") var radiantScore: Int,
    @Json(name = "dire_score") var direScore: Int,
    @Json(name = "radiant_win") var isRadiantWin: Boolean
)

data class CompetitiveUI(
    val matchId: Long,
    val startTime: Long,
    val duration: Int,
    val radiantName: String?,
    val direName: String?,
    val leagueName: String?,
    val radiantScore: Int,
    val direScore: Int,
    val isRadiantWin: Boolean
)

fun Competitive.mapToUi(): CompetitiveUI {
    return CompetitiveUI(
        matchId = match_id,
        startTime = start_time,
        duration = duration.toInt(),
        radiantName = radiant_name,
        direName = dire_name,
        leagueName = league_name,
        radiantScore = radiant_score.toInt(),
        direScore = dire_score.toInt(),
        isRadiantWin = radiant_win
    )
}