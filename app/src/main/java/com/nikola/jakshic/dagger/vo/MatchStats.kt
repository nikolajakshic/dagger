package com.nikola.jakshic.dagger.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MatchStats(
        @SerializedName("radiant_win") @Expose var isRadiantWin: Boolean,
        @SerializedName("radiant_team") @Expose var radiantTeam: Team?,
        @SerializedName("dire_team") @Expose var direTeam: Team?,
        @SerializedName("players") @Expose var players: List<PlayerStats>?)

data class Team(@SerializedName("name") @Expose var name: String?)