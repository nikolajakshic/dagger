package com.nikola.jakshic.dagger.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "search_history", indices = [Index(value = ["query"], unique = true)])
data class SearchHistory(
        @ColumnInfo(collate = ColumnInfo.NOCASE) var query: String) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}