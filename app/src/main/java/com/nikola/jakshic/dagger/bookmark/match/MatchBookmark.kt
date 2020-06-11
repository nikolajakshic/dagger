package com.nikola.jakshic.dagger.bookmark.match

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.nikola.jakshic.dagger.matchstats.MatchStats
import com.nikola.jakshic.dagger.matchstats.PlayerStats

@Entity(tableName = "bookmark_match", indices = [(Index(value = ["match_id"], unique = true))])
data class MatchBookmark(@ColumnInfo(name = "match_id") var matchId: Long) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
    @ColumnInfo(name = "note") var note: String? = null
}

// POJO for Database Query
class MatchesNotes {
    @ColumnInfo(name = "note") var note: String? = null
    @Embedded var matchStats: MatchStats? = null

    @Relation(
        entity = PlayerStats::class,
        entityColumn = "match_id",
        parentColumn = "match_id")
    var playerStats: List<PlayerStats>? = null
}