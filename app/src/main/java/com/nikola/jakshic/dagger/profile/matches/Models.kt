package com.nikola.jakshic.dagger.profile.matches

import com.nikola.jakshic.dagger.common.sqldelight.Matches
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchJson(
    var accountId: Long = 0,
    @Json(name = "match_id") var matchId: Long,
    @Json(name = "hero_id") var heroId: Long,
    @Json(name = "player_slot") var playerSlot: Long,
    @Json(name = "skill") var skill: Long,
    @Json(name = "duration") var duration: Long,
    @Json(name = "game_mode") var gameMode: Long,
    @Json(name = "lobby_type") var lobbyType: Long,
    @Json(name = "radiant_win") var isRadiantWin: Boolean,
    @Json(name = "start_time") var startTime: Long
)

data class MatchUI(
    val matchId: Long,
    val heroId: Long,
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
        heroId = this.heroId,
        playerSlot = this.playerSlot,
        skill = this.skill,
        duration = this.duration,
        gameMode = this.gameMode,
        lobbyType = this.lobbyType,
        isRadiantWin = this.isRadiantWin,
        startTime = this.startTime
    )
}

fun MatchJson.mapToDb(): Matches {
    return Matches(
        account_id = this.accountId,
        match_id = this.matchId,
        hero_id = this.heroId,
        player_slot = this.playerSlot,
        skill = this.skill,
        duration = this.duration,
        mode = this.gameMode,
        lobby = this.lobbyType,
        radiant_win = this.isRadiantWin,
        start_time = this.startTime
    )
}

fun Matches.mapToUi(): MatchUI {
    return MatchUI(
        matchId = this.match_id,
        heroId = this.hero_id,
        playerSlot = this.player_slot,
        skill = this.skill,
        duration = this.duration,
        gameMode = this.mode,
        lobbyType = this.lobby,
        isRadiantWin = this.radiant_win,
        startTime = this.start_time
    )
}

fun List<Matches>.mapToUi(): List<MatchUI> {
    val list = mutableListOf<MatchUI>()
    for (item in this) {
        list.add(item.mapToUi())
    }
    return list
}