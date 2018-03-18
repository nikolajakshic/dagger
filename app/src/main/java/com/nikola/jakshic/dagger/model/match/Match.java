
package com.nikola.jakshic.dagger.model.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nikola.jakshic.dagger.model.Player;

import java.util.List;

public class Match {

    @SerializedName("match_id") @Expose public long matchId;
    @SerializedName("hero_id") private long heroId;
    @SerializedName("player_slot") private long playerSlot;
    @SerializedName("skill") private long skill;
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
    @SerializedName("players") @Expose public List<Player> players = null;


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
}
