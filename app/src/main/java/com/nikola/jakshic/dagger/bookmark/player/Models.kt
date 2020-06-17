package com.nikola.jakshic.dagger.bookmark.player

import com.nikola.jakshic.dagger.common.sqldelight.Select
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllPlayerBookmark

data class PlayerBookmarkUI(
    val id: Long,
    var name: String?,
    var personaName: String?,
    var avatarUrl: String?,
    var rankTier: Int,
    var leaderboardRank: Int,
    var wins: Int,
    var losses: Int
)

fun Select.mapToUi(): PlayerBookmarkUI {
    return PlayerBookmarkUI(
        id = this.account_id,
        name = this.name,
        personaName = this.persona_name,
        avatarUrl = this.avatar_url,
        rankTier = this.rank_tier.toInt(),
        leaderboardRank = this.leaderboard_rank.toInt(),
        wins = this.wins.toInt(),
        losses = this.losses.toInt()
    )
}

fun List<SelectAllPlayerBookmark>.mapToUi(): List<PlayerBookmarkUI> {
    if (this.isEmpty()) return emptyList()

    val list = mutableListOf<PlayerBookmarkUI>()
    for (item in this) {
        list.add(
            PlayerBookmarkUI(
                item.account_id,
                item.name,
                item.persona_name,
                item.avatar_url,
                item.rank_tier.toInt(),
                item.leaderboard_rank.toInt(),
                item.wins.toInt(),
                item.losses.toInt()
            )
        )
    }
    return list
}