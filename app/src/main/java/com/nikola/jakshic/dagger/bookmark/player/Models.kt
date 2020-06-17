package com.nikola.jakshic.dagger.bookmark.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nikola.jakshic.dagger.common.sqldelight.Select
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllPlayerBookmark

@Entity(tableName = "bookmark", indices = [(Index(value = ["account_id"], unique = true))])
data class PlayerBookmark(@ColumnInfo(name = "account_id") var id: Long) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "count") var count: Int = 0
}

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