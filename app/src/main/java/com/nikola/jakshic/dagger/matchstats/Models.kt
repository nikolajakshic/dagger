package com.nikola.jakshic.dagger.matchstats

import com.nikola.jakshic.dagger.common.sqldelight.Match_stats
import com.nikola.jakshic.dagger.common.sqldelight.Player_stats
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllMatchStats
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MatchStatsJson {
    @Json(name = "match_id") val matchId: Long = 0
    @Json(name = "radiant_win") val isRadiantWin = false
    @Json(name = "dire_score") val direScore: Long = 0
    @Json(name = "radiant_score") val radiantScore: Long = 0
    @Json(name = "skill") val skill: Long = 0
    @Json(name = "game_mode") val mode: Long = 0
    @Json(name = "duration") val duration: Long = 0
    @Json(name = "start_time") val startTime: Long = 0
    @Json(name = "barracks_status_radiant") val radiantBarracks: Long = 0
    @Json(name = "barracks_status_dire") val direBarracks: Long = 0
    @Json(name = "tower_status_radiant") val radiantTowers: Long = 0
    @Json(name = "tower_status_dire") val direTowers: Long = 0
    @Json(name = "radiant_team") val radiantTeam: Team? = null
    @Json(name = "dire_team") val direTeam: Team? = null
    @Json(name = "players") val players: List<PlayerStatsJson>? = null
}

@JsonClass(generateAdapter = true)
data class Team(@Json(name = "name") val name: String?)

@JsonClass(generateAdapter = true)
data class PlayerStatsJson(
    @Json(name = "account_id") val id: Long,
    @Json(name = "match_id") val matchId: Long,
    @Json(name = "name") val name: String?,
    @Json(name = "personaname") val personaName: String?,
    @Json(name = "player_slot") val playerSlot: Long,
    @Json(name = "assists") val assists: Long,
    @Json(name = "backpack_0") val backpack0: Long,
    @Json(name = "backpack_1") val backpack1: Long,
    @Json(name = "backpack_2") val backpack2: Long,
    @Json(name = "deaths") val deaths: Long,
    @Json(name = "denies") val denies: Long,
    @Json(name = "gold_per_min") val goldPerMin: Long,
    @Json(name = "hero_damage") val heroDamage: Long,
    @Json(name = "hero_healing") val heroHealing: Long,
    @Json(name = "hero_id") val heroId: Long,
    @Json(name = "item_0") val item0: Long,
    @Json(name = "item_1") val item1: Long,
    @Json(name = "item_2") val item2: Long,
    @Json(name = "item_3") val item3: Long,
    @Json(name = "item_4") val item4: Long,
    @Json(name = "item_5") val item5: Long,
    @Json(name = "item_neutral") val itemNeutral: Long,
    @Json(name = "kills") val kills: Long,
    @Json(name = "last_hits") val lastHits: Long,
    @Json(name = "level") val level: Long,
    @Json(name = "tower_damage") val towerDamage: Long,
    @Json(name = "xp_per_min") val xpPerMin: Long,
    @Json(name = "purchase_ward_observer") val purchaseWardObserver: Long = 0,
    @Json(name = "purchase_ward_sentry") val purchaseWardSentry: Long = 0
)

data class MatchStatsUI(
    val matchId: Long,
    val isRadiantWin: Boolean,
    val direScore: Long,
    val radiantScore: Long,
    val skill: Long,
    val mode: Long,
    val duration: Long,
    val startTime: Long,
    val radiantBarracks: Long,
    val direBarracks: Long,
    val radiantTowers: Long,
    val direTowers: Long,
    val radiantName: String?,
    val direName: String?,
    val players: List<PlayerStatsUI>
) {
    data class PlayerStatsUI(
        val id: Long,
        val name: String?,
        val personaName: String?,
        val playerSlot: Long,
        val assists: Long,
        val backpack0: Long,
        val backpack1: Long,
        val backpack2: Long,
        val deaths: Long,
        val denies: Long,
        val goldPerMin: Long,
        val heroDamage: Long,
        val heroHealing: Long,
        val heroId: Long,
        val item0: Long,
        val item1: Long,
        val item2: Long,
        val item3: Long,
        val item4: Long,
        val item5: Long,
        val itemNeutral: Long,
        val kills: Long,
        val lastHits: Long,
        val level: Long,
        val towerDamage: Long,
        val xpPerMin: Long,
        val purchaseWardObserver: Long,
        val purchaseWardSentry: Long
    )
}

fun MatchStatsJson.mapToDb(): Match_stats {
    return Match_stats(
        match_id = matchId,
        radiant_win = isRadiantWin,
        dire_score = direScore,
        radiant_score = radiantScore,
        skill = skill,
        game_mode = mode,
        duration = duration,
        start_time = startTime,
        radiant_barracks = radiantBarracks,
        dire_barracks = direBarracks,
        radiant_towers = radiantTowers,
        dire_towers = direTowers,
        radiant_name = radiantTeam?.name,
        dire_name = direTeam?.name
    )
}

fun List<SelectAllMatchStats>.mapToUi(): MatchStatsUI? {
    if (isEmpty()) return null

    val players = mutableListOf<MatchStatsUI.PlayerStatsUI>()
    for (player in this) {
        players.add(
            MatchStatsUI.PlayerStatsUI(
                id = player.account_id,
                name = player.name,
                personaName = player.persona_name,
                playerSlot = player.player_slot,
                assists = player.assists,
                backpack0 = player.backpack_0,
                backpack1 = player.backpack_1,
                backpack2 = player.backpack_2,
                deaths = player.deaths,
                denies = player.denies,
                goldPerMin = player.gpm,
                heroDamage = player.hero_damage,
                heroHealing = player.hero_healing,
                heroId = player.hero_id,
                item0 = player.item_0,
                item1 = player.item_1,
                item2 = player.item_2,
                item3 = player.item_3,
                item4 = player.item_4,
                item5 = player.item_5,
                itemNeutral = player.item_neutral,
                kills = player.kills,
                lastHits = player.last_hits,
                level = player.level,
                towerDamage = player.tower_damage,
                xpPerMin = player.xpm,
                purchaseWardObserver = player.observers,
                purchaseWardSentry = player.sentries
            )
        )
    }
    return MatchStatsUI(
        matchId = this[0].match_id,
        isRadiantWin = this[0].radiant_win,
        direScore = this[0].dire_score,
        radiantScore = this[0].radiant_score,
        skill = this[0].skill,
        mode = this[0].game_mode,
        duration = this[0].duration,
        startTime = this[0].start_time,
        radiantBarracks = this[0].radiant_barracks,
        direBarracks = this[0].dire_barracks,
        radiantTowers = this[0].radiant_towers,
        direTowers = this[0].dire_towers,
        radiantName = this[0].radiant_name,
        direName = this[0].dire_name,
        players = players
    )
}

fun PlayerStatsJson.mapToDb(): Player_stats {
    return Player_stats(
        account_id = id,
        match_id = matchId,
        name = name,
        persona_name = personaName,
        player_slot = playerSlot,
        assists = assists,
        backpack_0 = backpack0,
        backpack_1 = backpack1,
        backpack_2 = backpack2,
        deaths = deaths,
        denies = denies,
        gpm = goldPerMin,
        hero_damage = heroDamage,
        hero_healing = heroHealing,
        hero_id = heroId,
        item_0 = item0,
        item_1 = item1,
        item_2 = item2,
        item_3 = item3,
        item_4 = item4,
        item_5 = item5,
        item_neutral = itemNeutral,
        kills = kills,
        last_hits = lastHits,
        level = level,
        tower_damage = towerDamage,
        xpm = xpPerMin,
        observers = purchaseWardObserver,
        sentries = purchaseWardSentry
    )
}