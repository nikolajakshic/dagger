package com.nikola.jakshic.dagger.matchstats

import com.nikola.jakshic.dagger.common.sqldelight.Match_stats
import com.nikola.jakshic.dagger.common.sqldelight.Player_stats
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllMatchStats
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MatchStatsJson {
    @Json(name = "match_id") var matchId: Long = 0
    @Json(name = "radiant_win") var isRadiantWin = false
    @Json(name = "dire_score") var direScore: Long = 0
    @Json(name = "radiant_score") var radiantScore: Long = 0
    @Json(name = "skill") var skill: Long = 0
    @Json(name = "game_mode") var mode: Long = 0
    @Json(name = "duration") var duration: Long = 0
    @Json(name = "start_time") var startTime: Long = 0
    @Json(name = "barracks_status_radiant") var radiantBarracks: Long = 0
    @Json(name = "barracks_status_dire") var direBarracks: Long = 0
    @Json(name = "tower_status_radiant") var radiantTowers: Long = 0
    @Json(name = "tower_status_dire") var direTowers: Long = 0
    @Json(name = "radiant_team") var radiantTeam: Team? = null
    @Json(name = "dire_team") var direTeam: Team? = null
    @Json(name = "players") var players: List<PlayerStatsJson>? = null
}

@JsonClass(generateAdapter = true)
data class Team(@Json(name = "name") var name: String?)

@JsonClass(generateAdapter = true)
data class PlayerStatsJson(
    @Json(name = "account_id") var id: Long,
    @Json(name = "match_id") var matchId: Long,
    @Json(name = "name") var name: String?,
    @Json(name = "personaname") var personaName: String?,
    @Json(name = "player_slot") var playerSlot: Long,
    @Json(name = "assists") var assists: Long,
    @Json(name = "backpack_0") var backpack0: Long,
    @Json(name = "backpack_1") var backpack1: Long,
    @Json(name = "backpack_2") var backpack2: Long,
    @Json(name = "deaths") var deaths: Long,
    @Json(name = "denies") var denies: Long,
    @Json(name = "gold_per_min") var goldPerMin: Long,
    @Json(name = "hero_damage") var heroDamage: Long,
    @Json(name = "hero_healing") var heroHealing: Long,
    @Json(name = "hero_id") var heroId: Long,
    @Json(name = "item_0") var item0: Long,
    @Json(name = "item_1") var item1: Long,
    @Json(name = "item_2") var item2: Long,
    @Json(name = "item_3") var item3: Long,
    @Json(name = "item_4") var item4: Long,
    @Json(name = "item_5") var item5: Long,
    @Json(name = "item_neutral") var itemNeutral: Long,
    @Json(name = "kills") var kills: Long,
    @Json(name = "last_hits") var lastHits: Long,
    @Json(name = "level") var level: Long,
    @Json(name = "tower_damage") var towerDamage: Long,
    @Json(name = "xp_per_min") var xpPerMin: Long,
    @Json(name = "purchase_ward_observer") var purchaseWardObserver: Long = 0,
    @Json(name = "purchase_ward_sentry") var purchaseWardSentry: Long = 0
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