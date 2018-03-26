package com.nikola.jakshic.dagger.model.match;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nikola.jakshic.dagger.model.Player;

import java.util.List;

@Entity(tableName = "matches", primaryKeys = {"match_id", "account_id"})
public class Match {

    @ColumnInfo(name = "account_id") private long accountId;
    @ColumnInfo(name = "match_id")@SerializedName("match_id") @Expose public long matchId;
    @ColumnInfo(name = "hero_id") @SerializedName("hero_id") @Expose private long heroId;
    @ColumnInfo(name = "player_slot") @SerializedName("player_slot") @Expose private long playerSlot;
    @ColumnInfo(name = "skill") @SerializedName("skill") private @Expose long skill;
    @Ignore @SerializedName("dire_score") @Expose public long direScore;
    @ColumnInfo(name = "duration")@SerializedName("duration") @Expose public long duration;
    @ColumnInfo(name = "mode") @SerializedName("game_mode") @Expose public long gameMode;
    @ColumnInfo(name = "lobby") @SerializedName("lobby_type") @Expose public long lobbyType;
    @Ignore @SerializedName("picks_bans") @Expose public List<PicksBan> picksBans = null;
    @Ignore @SerializedName("radiant_gold_adv") @Expose public List<Long> radiantGoldAdv = null;
    @Ignore @SerializedName("radiant_score") @Expose public long radiantScore;
    @ColumnInfo(name = "radiant_win") @SerializedName("radiant_win") @Expose public boolean radiantWin;
    @Ignore @SerializedName("radiant_xp_adv") @Expose public List<Long> radiantXpAdv = null;
    @ColumnInfo(name = "start_time") @SerializedName("start_time") @Expose public long startTime;
    @Ignore @SerializedName("league") @Expose public League league;
    @Ignore @SerializedName("radiant_team") @Expose public RadiantTeam radiantTeam;
    @Ignore @SerializedName("dire_team") @Expose public DireTeam direTeam;
    @Ignore @SerializedName("players") @Expose public List<Player> players = null;

    public long getMatchId() {
        return matchId;
    }

    public long getHeroId() {
        return heroId;
    }

    public long getPlayerSlot() {
        return playerSlot;
    }

    public long getSkill() {
        return skill;
    }

    public long getDireScore() {
        return direScore;
    }

    public long getDuration() {
        return duration;
    }

    public long getGameMode() {
        return gameMode;
    }

    public long getLobbyType() {
        return lobbyType;
    }

    public void setRadiantWin(boolean radiantWin) {
        this.radiantWin = radiantWin;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public void setHeroId(long heroId) {
        this.heroId = heroId;
    }

    public void setPlayerSlot(long playerSlot) {
        this.playerSlot = playerSlot;
    }

    public void setSkill(long skill) {
        this.skill = skill;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setGameMode(long gameMode) {
        this.gameMode = gameMode;
    }

    public void setLobbyType(long lobbyType) {
        this.lobbyType = lobbyType;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public List<PicksBan> getPicksBans() {
        return picksBans;
    }

    public List<Long> getRadiantGoldAdv() {
        return radiantGoldAdv;
    }

    public long getRadiantScore() {
        return radiantScore;
    }

    public boolean isRadiantWin() {
        return radiantWin;
    }

    public List<Long> getRadiantXpAdv() {
        return radiantXpAdv;
    }

    public long getStartTime() {
        return startTime;
    }

    public League getLeague() {
        return league;
    }

    public RadiantTeam getRadiantTeam() {
        return radiantTeam;
    }

    public DireTeam getDireTeam() {
        return direTeam;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        if (accountId != match.accountId) return false;
        if (matchId != match.matchId) return false;
        if (heroId != match.heroId) return false;
        if (playerSlot != match.playerSlot) return false;
        if (skill != match.skill) return false;
        if (duration != match.duration) return false;
        if (gameMode != match.gameMode) return false;
        if (lobbyType != match.lobbyType) return false;
        if (radiantWin != match.radiantWin) return false;
        return startTime == match.startTime;
    }

    @Override
    public int hashCode() {
        int result = (int) (accountId ^ (accountId >>> 32));
        result = 31 * result + (int) (matchId ^ (matchId >>> 32));
        result = 31 * result + (int) (heroId ^ (heroId >>> 32));
        result = 31 * result + (int) (playerSlot ^ (playerSlot >>> 32));
        result = 31 * result + (int) (skill ^ (skill >>> 32));
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (int) (gameMode ^ (gameMode >>> 32));
        result = 31 * result + (int) (lobbyType ^ (lobbyType >>> 32));
        result = 31 * result + (radiantWin ? 1 : 0);
        result = 31 * result + (int) (startTime ^ (startTime >>> 32));
        return result;
    }
}