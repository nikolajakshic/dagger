package com.nikola.jakshic.dagger.matchstats

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.nikola.jakshic.dagger.common.sqldelight.Match_stats
import com.nikola.jakshic.dagger.common.sqldelight.Player_stats
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllMatchStats
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "match_stats")
@JsonClass(generateAdapter = true)
class MatchStatsJson {
    @PrimaryKey @ColumnInfo(name = "match_id") @Json(name = "match_id") var matchId: Long = 0
    @ColumnInfo(name = "radiant_win") @Json(name = "radiant_win") var isRadiantWin = false
    @ColumnInfo(name = "dire_score") @Json(name = "dire_score") var direScore: Int = 0
    @ColumnInfo(name = "radiant_score") @Json(name = "radiant_score") var radiantScore: Int = 0
    @ColumnInfo(name = "skill") @Json(name = "skill") var skill: Int = 0
    @ColumnInfo(name = "game_mode") @Json(name = "game_mode") var mode: Int = 0
    @ColumnInfo(name = "duration") @Json(name = "duration") var duration: Int = 0
    @ColumnInfo(name = "start_time") @Json(name = "start_time") var startTime: Long = 0
    @ColumnInfo(name = "radiant_barracks") @Json(name = "barracks_status_radiant") var radiantBarracks: Int = 0
    @ColumnInfo(name = "dire_barracks") @Json(name = "barracks_status_dire") var direBarracks: Int = 0
    @ColumnInfo(name = "radiant_towers") @Json(name = "tower_status_radiant") var radiantTowers: Int = 0
    @ColumnInfo(name = "dire_towers") @Json(name = "tower_status_dire") var direTowers: Int = 0
    @Embedded(prefix = "radiant_") @Json(name = "radiant_team") var radiantTeam: Team? = null
    @Embedded(prefix = "dire_") @Json(name = "dire_team") var direTeam: Team? = null
    @Ignore @Json(name = "players") var players: List<PlayerStatsJson>? = null
}

@JsonClass(generateAdapter = true)
data class Team(@Json(name = "name") var name: String?)

@Entity(tableName = "player_stats",
    primaryKeys = ["match_id", "account_id", "player_slot"],
    foreignKeys = [(ForeignKey(
        entity = MatchStatsJson::class,
        parentColumns = ["match_id"],
        childColumns = ["match_id"],
        onDelete = ForeignKey.CASCADE))])
@JsonClass(generateAdapter = true)
data class PlayerStatsJson(
    @ColumnInfo(name = "account_id") @Json(name = "account_id") var id: Long,
    @ColumnInfo(name = "match_id") @Json(name = "match_id") var matchId: Long,
    @ColumnInfo(name = "name") @Json(name = "name") var name: String?,
    @ColumnInfo(name = "persona_name") @Json(name = "personaname") var personaName: String?,
    @ColumnInfo(name = "player_slot") @Json(name = "player_slot") var playerSlot: Int,
    @ColumnInfo(name = "assists") @Json(name = "assists") var assists: Int,
    @ColumnInfo(name = "backpack_0") @Json(name = "backpack_0") var backpack0: Int,
    @ColumnInfo(name = "backpack_1") @Json(name = "backpack_1") var backpack1: Int,
    @ColumnInfo(name = "backpack_2") @Json(name = "backpack_2") var backpack2: Int,
    @ColumnInfo(name = "deaths") @Json(name = "deaths") var deaths: Int,
    @ColumnInfo(name = "denies") @Json(name = "denies") var denies: Int,
    @ColumnInfo(name = "gpm") @Json(name = "gold_per_min") var goldPerMin: Int,
    @ColumnInfo(name = "hero_damage") @Json(name = "hero_damage") var heroDamage: Int,
    @ColumnInfo(name = "hero_healing") @Json(name = "hero_healing") var heroHealing: Int,
    @ColumnInfo(name = "hero_id") @Json(name = "hero_id") var heroId: Int,
    @ColumnInfo(name = "item_0") @Json(name = "item_0") var item0: Int,
    @ColumnInfo(name = "item_1") @Json(name = "item_1") var item1: Int,
    @ColumnInfo(name = "item_2") @Json(name = "item_2") var item2: Int,
    @ColumnInfo(name = "item_3") @Json(name = "item_3") var item3: Int,
    @ColumnInfo(name = "item_4") @Json(name = "item_4") var item4: Int,
    @ColumnInfo(name = "item_5") @Json(name = "item_5") var item5: Int,
    @ColumnInfo(name = "item_neutral") @Json(name = "item_neutral") var itemNeutral: Int,
    @ColumnInfo(name = "kills") @Json(name = "kills") var kills: Int,
    @ColumnInfo(name = "last_hits") @Json(name = "last_hits") var lastHits: Int,
    @ColumnInfo(name = "level") @Json(name = "level") var level: Int,
    @ColumnInfo(name = "tower_damage") @Json(name = "tower_damage") var towerDamage: Int,
    @ColumnInfo(name = "xpm") @Json(name = "xp_per_min") var xpPerMin: Int,
    @ColumnInfo(name = "observers") @Json(name = "purchase_ward_observer") var purchaseWardObserver: Int = 0,
    @ColumnInfo(name = "sentries") @Json(name = "purchase_ward_sentry") var purchaseWardSentry: Int = 0
)

// POJO for Database Query
class Stats {
    @Embedded
    var matchStats: MatchStatsJson? = null

    @Relation(
        entity = PlayerStatsJson::class,
        entityColumn = "match_id",
        parentColumn = "match_id")
    var playerStats: List<PlayerStatsJson>? = null
}

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
        dire_score = direScore.toLong(),
        radiant_score = radiantScore.toLong(),
        skill = skill.toLong(),
        game_mode = mode.toLong(),
        duration = duration.toLong(),
        start_time = startTime,
        radiant_barracks = radiantBarracks.toLong(),
        dire_barracks = direBarracks.toLong(),
        radiant_towers = radiantTowers.toLong(),
        dire_towers = direTowers.toLong(),
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
        player_slot = playerSlot.toLong(),
        assists = assists.toLong(),
        backpack_0 = backpack0.toLong(),
        backpack_1 = backpack1.toLong(),
        backpack_2 = backpack2.toLong(),
        deaths = deaths.toLong(),
        denies = denies.toLong(),
        gpm = goldPerMin.toLong(),
        hero_damage = heroDamage.toLong(),
        hero_healing = heroHealing.toLong(),
        hero_id = heroId.toLong(),
        item_0 = item0.toLong(),
        item_1 = item1.toLong(),
        item_2 = item2.toLong(),
        item_3 = item3.toLong(),
        item_4 = item4.toLong(),
        item_5 = item5.toLong(),
        item_neutral = itemNeutral.toLong(),
        kills = kills.toLong(),
        last_hits = lastHits.toLong(),
        level = level.toLong(),
        tower_damage = towerDamage.toLong(),
        xpm = xpPerMin.toLong(),
        observers = purchaseWardObserver.toLong(),
        sentries = purchaseWardSentry.toLong()
    )
}