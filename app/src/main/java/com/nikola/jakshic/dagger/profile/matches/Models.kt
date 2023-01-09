package com.nikola.jakshic.dagger.profile.matches

import com.nikola.jakshic.dagger.common.sqldelight.Matches
import com.nikola.jakshic.dagger.common.sqldelight.match.SelectAll
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchJson(
    @Json(name = "match_id") val matchId: Long,
    @Json(name = "hero_id") val heroId: Long,
    @Json(name = "player_slot") val playerSlot: Long,
    @Json(name = "skill") val skill: Long,
    @Json(name = "duration") val duration: Long,
    @Json(name = "game_mode") val gameMode: Long,
    @Json(name = "lobby_type") val lobbyType: Long,
    @Json(name = "radiant_win") val isRadiantWin: Boolean,
    @Json(name = "start_time") val startTime: Long
)

data class MatchUI(
    val matchId: Long,
    val heroImage: String?,
    val playerSlot: Long,
    val skill: Long,
    val duration: Long,
    val gameMode: Long,
    val lobbyType: Long,
    val isRadiantWin: Boolean,
    val startTime: Long
)

fun MatchJson.mapToUi(): MatchUI {
    return MatchUI(
        matchId = this.matchId,
        heroImage = null,
        playerSlot = this.playerSlot,
        skill = this.skill,
        duration = this.duration,
        gameMode = this.gameMode,
        lobbyType = this.lobbyType,
        isRadiantWin = this.isRadiantWin,
        startTime = this.startTime
    )
}

fun MatchJson.mapToDb(accountId: Long): Matches {
    return Matches(
        account_id = accountId,
        match_id = this.matchId,
        hero_id = this.heroId,
        player_slot = this.playerSlot,
        skill = this.skill,
        duration = this.duration,
        mode = this.gameMode,
        lobby = this.lobbyType,
        radiant_win = if (this.isRadiantWin) 1 else 0,
        start_time = this.startTime
    )
}

fun SelectAll.mapToUi(): MatchUI {
    return MatchUI(
        matchId = this.match_id,
        heroImage = this.hero_image,
        playerSlot = this.player_slot,
        skill = this.skill,
        duration = this.duration,
        gameMode = this.mode,
        lobbyType = this.lobby,
        isRadiantWin = this.radiant_win == 1L,
        startTime = this.start_time
    )
}
