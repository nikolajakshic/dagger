package com.nikola.jakshic.dagger.bookmark.player

import com.nikola.jakshic.dagger.common.sqldelight.Select
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllPlayerBookmark

data class PlayerBookmarkUI(
    val id: Long,
    var name: String?,
    var personaName: String?,
    var avatarUrl: String?,
    var rankTier: Long,
    var leaderboardRank: Long,
    var wins: Long,
    var losses: Long,
)

fun Select.mapToUi(): PlayerBookmarkUI {
    return PlayerBookmarkUI(
        id = this.account_id,
        name = this.name,
        personaName = this.persona_name,
        avatarUrl = this.avatar_url,
        rankTier = this.rank_tier,
        leaderboardRank = this.leaderboard_rank,
        wins = this.wins,
        losses = this.losses,
    )
}

fun List<SelectAllPlayerBookmark>.mapToUi(): List<PlayerBookmarkUI> {
    val list = mutableListOf<PlayerBookmarkUI>()
    for (item in this) {
        list.add(
            PlayerBookmarkUI(
                item.account_id,
                item.name,
                item.persona_name,
                item.avatar_url,
                item.rank_tier,
                item.leaderboard_rank,
                item.wins,
                item.losses,
            ),
        )
    }
    return list
}
