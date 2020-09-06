package com.nikola.jakshic.dagger.bookmark.match

import com.nikola.jakshic.dagger.common.sqldelight.SelectAllMatchBookmark
import com.nikola.jakshic.dagger.matchstats.MatchStatsUI

class MatchBookmarkUI(
    val matchId: Long,
    val note: String?,
    val matchStats: MatchStatsUI
)

fun List<SelectAllMatchBookmark>.mapToUi(): List<MatchBookmarkUI> {
    val map = this.groupBy(SelectAllMatchBookmark::match_id)
    val matchBookmarkList = mutableListOf<MatchBookmarkUI>()

    for (item in map) {
        val playerStatsList = mutableListOf<MatchStatsUI.PlayerStatsUI>()
        if (item.value.isEmpty()) continue // with this, we are safe to call item.value[0]

        item.value.forEach { playerStatsList.add(it.mapToPlayerStatsUi()) }
        val matchStats = MatchStatsUI(
            matchId = item.value[0].match_id,
            isRadiantWin = item.value[0].radiant_win,
            direScore = item.value[0].dire_score,
            radiantScore = item.value[0].radiant_score,
            skill = item.value[0].skill,
            mode = item.value[0].game_mode,
            duration = item.value[0].duration,
            startTime = item.value[0].start_time,
            radiantBarracks = item.value[0].radiant_barracks,
            direBarracks = item.value[0].dire_barracks,
            radiantTowers = item.value[0].radiant_towers,
            direTowers = item.value[0].dire_towers,
            radiantName = item.value[0].radiant_name,
            direName = item.value[0].dire_name,
            players = playerStatsList
        )

        matchBookmarkList.add(
            MatchBookmarkUI(
                matchId = item.value[0].match_id,
                note = item.value[0].note,
                matchStats = matchStats
            )
        )
    }
    return matchBookmarkList
}

private fun SelectAllMatchBookmark.mapToPlayerStatsUi(): MatchStatsUI.PlayerStatsUI {
    return MatchStatsUI.PlayerStatsUI(
        id = this.account_id,
        name = this.name,
        personaName = this.persona_name,
        playerSlot = this.player_slot,
        assists = this.assists,
        backpack0 = this.backpack_0,
        backpack1 = this.backpack_1,
        backpack2 = this.backpack_2,
        deaths = this.deaths,
        denies = this.denies,
        goldPerMin = this.gpm,
        heroDamage = this.hero_damage,
        heroHealing = this.hero_healing,
        heroId = this.hero_id,
        item0 = this.item_0,
        item1 = this.item_1,
        item2 = this.item_2,
        item3 = this.item_3,
        item4 = this.item_4,
        item5 = this.item_5,
        itemNeutral = this.item_neutral,
        kills = this.kills,
        lastHits = this.last_hits,
        level = this.level,
        towerDamage = this.tower_damage,
        xpPerMin = this.xpm,
        purchaseWardObserver = this.observers,
        purchaseWardSentry = this.sentries
    )
}