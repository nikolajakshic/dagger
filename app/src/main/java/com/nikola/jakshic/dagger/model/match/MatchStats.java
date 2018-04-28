package com.nikola.jakshic.dagger.model.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatchStats {

    @SerializedName("match_id") @Expose public long matchId;
    @SerializedName("hero_id") @Expose private long heroId;
    @SerializedName("player_slot") @Expose private long playerSlot;
    @SerializedName("skill") private @Expose long skill;

    @SerializedName("dire_score") @Expose public long direScore;
    @SerializedName("duration") @Expose public long duration;
    @SerializedName("game_mode") @Expose public long gameMode;
    @SerializedName("lobby_type") @Expose public long lobbyType;
    @SerializedName("picks_bans") @Expose public List<PicksBan> picksBans = null;
    @SerializedName("radiant_gold_adv") @Expose public List<Long> radiantGoldAdv = null;
    @SerializedName("radiant_score") @Expose public long radiantScore;
    @SerializedName("radiant_win") @Expose public boolean radiantWin;
    @SerializedName("radiant_xp_adv") @Expose public List<Long> radiantXpAdv = null;
    @SerializedName("start_time") @Expose public long startTime;
    @SerializedName("league") @Expose public League league;
    @SerializedName("radiant_team") @Expose public RadiantTeam radiantTeam;
    @SerializedName("dire_team") @Expose public DireTeam direTeam;
   @SerializedName("players") @Expose public List<PlayerStats> players = null;

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

    public List<PlayerStats> getPlayers() {
        return players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchStats match = (MatchStats) o;

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
        int result = (int) (matchId ^ (matchId >>> 32));
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