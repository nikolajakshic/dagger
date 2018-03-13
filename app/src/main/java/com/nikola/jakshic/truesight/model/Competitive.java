package com.nikola.jakshic.truesight.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Competitive {

    @SerializedName("match_id") @Expose private long matchId;
    @SerializedName("radiant_name") @Expose private String radiantName;
    @SerializedName("dire_name") @Expose private String direName;
    @SerializedName("league_name") @Expose private String leagueName;
    @SerializedName("radiant_score") @Expose private long radiantScore;
    @SerializedName("dire_score") @Expose private long direScore;
    @SerializedName("radiant_win") @Expose private boolean radiantWin;

    public long getMatchId() {
        return matchId;
    }

    public String getRadiantName() {
        return radiantName;
    }

    public String getDireName() {
        return direName;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public long getRadiantScore() {
        return radiantScore;
    }

    public long getDireScore() {
        return direScore;
    }

    public boolean isRadiantWin() {
        return radiantWin;
    }
}