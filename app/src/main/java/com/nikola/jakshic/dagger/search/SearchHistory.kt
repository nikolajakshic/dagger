package com.nikola.jakshic.dagger.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "search_history", indices = [Index(value = ["query"], unique = true)])
data class SearchHistory(
        @ColumnInfo(collate = ColumnInfo.NOCASE) var query: String) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}