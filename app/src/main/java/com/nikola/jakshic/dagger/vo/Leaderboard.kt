package com.nikola.jakshic.dagger.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class _Leaderboard(@Json(name = "leaderboard") val leaderboard: List<Leaderboard>?)

@Entity(tableName = "leaderboards")
@JsonClass(generateAdapter = true)
data class Leaderboard(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @Json(name = "name") var name: String?,
        var region: String?)

enum class Region {
    AMERICAS,
    CHINA,
    EUROPE,
    SE_ASIA
}