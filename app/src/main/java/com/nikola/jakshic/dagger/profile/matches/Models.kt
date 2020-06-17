package com.nikola.jakshic.dagger.profile.matches

import com.nikola.jakshic.dagger.common.sqldelight.Matches
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchJson(
    var accountId: Long = 0,
    @Json(name = "match_id") var matchId: Long,
    @Json(name = "hero_id") var heroId: Int,
    @Json(name = "player_slot") var playerSlot: Int,
    @Json(name = "skill") var skill: Int,
    @Json(name = "duration") var duration: Int,
    @Json(name = "game_mode") var gameMode: Int,
    @Json(name = "lobby_type") var lobbyType: Int,
    @Json(name = "radiant_win") var isRadiantWin: Boolean,
    @Json(name = "start_time") var startTime: Long
)

data class MatchUI(
    val matchId: Long,
    val heroId: Int,
    val playerSlot: Int,
    val skill: Int,
    val duration: Int,
    val gameMode: Int,
    val lobbyType: Int,
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
        hero_id = this.heroId.toLong(),
        player_slot = this.playerSlot.toLong(),
        skill = this.skill.toLong(),
        duration = this.duration.toLong(),
        mode = this.gameMode.toLong(),
        lobby = this.lobbyType.toLong(),
        radiant_win = this.isRadiantWin,
        start_time = this.startTime
    )
}

fun Matches.mapToUi(): MatchUI {
    return MatchUI(
        matchId = this.match_id,
        heroId = this.hero_id.toInt(),
        playerSlot = this.player_slot.toInt(),
        skill = this.skill.toInt(),
        duration = this.duration.toInt(),
        gameMode = this.mode.toInt(),
        lobbyType = this.lobby.toInt(),
        isRadiantWin = this.radiant_win,
        startTime = this.start_time
    )
}

fun mapToUi(
    account_id: Long,
    match_id: Long,
    hero_id: Long,
    player_slot: Long,
    skill: Long,
    duration: Long,
    mode: Long,
    lobby: Long,
    radiant_win: Boolean,
    start_time: Long
): MatchUI {
    return MatchUI(
        matchId = match_id,
        heroId = hero_id.toInt(),
        playerSlot = player_slot.toInt(),
        skill = skill.toInt(),
        duration = duration.toInt(),
        gameMode = mode.toInt(),
        lobbyType = lobby.toInt(),
        isRadiantWin = radiant_win,
        startTime = start_time
    )
}