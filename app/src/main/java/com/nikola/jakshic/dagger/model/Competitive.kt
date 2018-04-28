package com.nikola.jakshic.dagger.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "competitive")
data class Competitive(
        @Expose @PrimaryKey @ColumnInfo(name = "match_id") @SerializedName("match_id") var matchId: Long,
        @Expose @ColumnInfo(name = "start_time") @SerializedName("start_time") var startTime: Long,
        @Expose @ColumnInfo(name = "duration") @SerializedName("duration") var duration: Long,
        @Expose @ColumnInfo(name = "radiant_name") @SerializedName("radiant_name") var radiantName: String?,
        @Expose @ColumnInfo(name = "dire_name") @SerializedName("dire_name") var direName: String?,
        @Expose @ColumnInfo(name = "league_name") @SerializedName("league_name") var leagueName: String?,
        @Expose @ColumnInfo(name = "radiant_score") @SerializedName("radiant_score") var radiantScore: Long,
        @Expose @ColumnInfo(name = "dire_score") @SerializedName("dire_score") var direScore: Long,
        @Expose @ColumnInfo(name = "radiant_win") @SerializedName("radiant_win") var isRadiantWin: Boolean
)