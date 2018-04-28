package com.nikola.jakshic.dagger.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose

class _Leaderboard(@Expose val leaderboard: List<Leaderboard>)

@Entity(tableName = "leaderboards")
data class Leaderboard(@PrimaryKey(autoGenerate = true) var id: Int, @Expose var name: String, var region: String)

enum class Region {
    AMERICAS,
    CHINA,
    EUROPE,
    SE_ASIA
}