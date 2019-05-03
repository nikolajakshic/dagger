package com.nikola.jakshic.dagger.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "heroes", primaryKeys = arrayOf("account_id", "hero_id"))
@JsonClass(generateAdapter = true)
data class Hero(
        @ColumnInfo(name = "account_id") var accountId: Long = 0,
        @ColumnInfo(name = "hero_id") @Json(name = "hero_id") var heroId: Int,
        @ColumnInfo(name = "games") @Json(name = "games") var gamesPlayed: Int,
        @ColumnInfo(name = "wins") @Json(name = "win") var gamesWon: Int)