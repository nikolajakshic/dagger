package com.nikola.jakshic.dagger.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class _Leaderboard(@SerializedName("leaderboard") @Expose val leaderboard: List<Leaderboard>?)

@Entity(tableName = "leaderboards")
data class Leaderboard(@PrimaryKey(autoGenerate = true) var id: Int, @SerializedName("name") @Expose var name: String?, var region: String?)

enum class Region {
    AMERICAS,
    CHINA,
    EUROPE,
    SE_ASIA
}