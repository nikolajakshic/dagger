package com.nikola.jakshic.truesight.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "competitive")
public class Competitive {

    @PrimaryKey @ColumnInfo(name = "match_id") @SerializedName("match_id") @Expose private long matchId;
    @ColumnInfo(name = "start_time") @SerializedName("start_time") @Expose private long startTime;
    @ColumnInfo(name = "duration") @SerializedName("duration") @Expose private long duration;
    @ColumnInfo(name = "radiant_name") @SerializedName("radiant_name") @Expose private String radiantName;
    @ColumnInfo(name = "dire_name") @SerializedName("dire_name") @Expose private String direName;
    @ColumnInfo(name = "league_name") @SerializedName("league_name") @Expose private String leagueName;
    @ColumnInfo(name = "radiant_score") @SerializedName("radiant_score") @Expose private long radiantScore;
    @ColumnInfo(name = "dire_score") @SerializedName("dire_score") @Expose private long direScore;
    @ColumnInfo(name = "radiant_win") @SerializedName("radiant_win") @Expose private boolean radiantWin;

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setRadiantName(String radiantName) {
        this.radiantName = radiantName;
    }

    public void setDireName(String direName) {
        this.direName = direName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public void setRadiantScore(long radiantScore) {
        this.radiantScore = radiantScore;
    }

    public void setDireScore(long direScore) {
        this.direScore = direScore;
    }

    public void setRadiantWin(boolean radiantWin) {
        this.radiantWin = radiantWin;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Competitive that = (Competitive) o;

        if (matchId != that.matchId) return false;
        if (startTime != that.startTime) return false;
        if (duration != that.duration) return false;
        if (radiantScore != that.radiantScore) return false;
        if (direScore != that.direScore) return false;
        if (radiantWin != that.radiantWin) return false;
        if (radiantName != null ? !radiantName.equals(that.radiantName) : that.radiantName != null)
            return false;
        if (direName != null ? !direName.equals(that.direName) : that.direName != null)
            return false;
        return leagueName != null ? leagueName.equals(that.leagueName) : that.leagueName == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (matchId ^ (matchId >>> 32));
        result = 31 * result + (int) (startTime ^ (startTime >>> 32));
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (radiantName != null ? radiantName.hashCode() : 0);
        result = 31 * result + (direName != null ? direName.hashCode() : 0);
        result = 31 * result + (leagueName != null ? leagueName.hashCode() : 0);
        result = 31 * result + (int) (radiantScore ^ (radiantScore >>> 32));
        result = 31 * result + (int) (direScore ^ (direScore >>> 32));
        result = 31 * result + (radiantWin ? 1 : 0);
        return result;
    }
}