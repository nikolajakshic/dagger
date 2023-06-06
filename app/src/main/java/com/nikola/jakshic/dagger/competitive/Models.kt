package com.nikola.jakshic.dagger.competitive

import com.nikola.jakshic.dagger.common.sqldelight.Competitive
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitiveJson(
    @Json(name = "match_id") val matchId: Long,
    @Json(name = "start_time") val startTime: Long,
    @Json(name = "duration") val duration: Long,
    @Json(name = "radiant_name") val radiantName: String?,
    @Json(name = "dire_name") val direName: String?,
    @Json(name = "league_name") val leagueName: String?,
    @Json(name = "radiant_score") val radiantScore: Long,
    @Json(name = "dire_score") val direScore: Long,
    @Json(name = "radiant_win") val isRadiantWin: Boolean,
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
    val isRadiantWin: Boolean,
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
        isRadiantWin = radiant_win == 1L,
    )
}
