package com.nikola.jakshic.dagger.competitive

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