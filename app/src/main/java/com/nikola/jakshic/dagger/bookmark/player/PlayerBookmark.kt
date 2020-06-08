package com.nikola.jakshic.dagger.bookmark.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark", indices = [(Index(value = ["account_id"], unique = true))])
data class PlayerBookmark(@ColumnInfo(name = "account_id") var id: Long) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "count") var count: Int = 0
}