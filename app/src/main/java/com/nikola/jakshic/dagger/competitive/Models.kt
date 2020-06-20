package com.nikola.jakshic.dagger.competitive

import com.nikola.jakshic.dagger.common.sqldelight.Competitive
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitiveJson(
    @Json(name = "match_id") var matchId: Long,
    @Json(name = "start_time") var startTime: Long,
    @Json(name = "duration") var duration: Long,
    @Json(name = "radiant_name") var radiantName: String?,
    @Json(name = "dire_name") var direName: String?,
    @Json(name = "league_name") var leagueName: String?,
    @Json(name = "radiant_score") var radiantScore: Long,
    @Json(name = "dire_score") var direScore: Long,
    @Json(name = "radiant_win") var isRadiantWin: Boolean
)

data class CompetitiveUI(
    val matchId: Long,
    val startTime: Long,
    val duration: Long,
    val radiantName: String?,
    val direName: String?,
    val leagueName: String?,
    val radiantScore: Long,
    val direScore: Long,
    val isRadiantWin: Boolean
)

fun Competitive.mapToUi(): CompetitiveUI {
    return CompetitiveUI(
        matchId = match_id,
        startTime = start_time,
        duration = duration,
        radiantName = radiant_name,
        direName = dire_name,
        leagueName = league_name,
        radiantScore = radiant_score,
        direScore = dire_score,
        isRadiantWin = radiant_win
    )
}