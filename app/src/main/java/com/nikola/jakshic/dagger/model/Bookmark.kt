package com.nikola.jakshic.dagger.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "bookmark")
data class Bookmark(@PrimaryKey @ColumnInfo(name = "account_id") var id: Long)