package com.nikola.jakshic.truesight.model;

import com.google.gson.annotations.SerializedName;

public class Match {

    @SerializedName("match_id")
    private long matchID;
    @SerializedName("hero_id")
    private long heroID;
    @SerializedName("radiant_win")
    private boolean radiantWon;
    @SerializedName("player_slot")
    private long playerSlot;
    @SerializedName("skill")
    private int skillLevel;
    @SerializedName("lobby_type")
    private int lobbyType;
    @SerializedName("game_mode")
    private int gameMode;
    @SerializedName("duration")
    private long duration;
    @SerializedName("start_time")
    private long startTime;

    public long getMatchID() {
        return matchID;
    }

    public long getHeroID() {
        return heroID;
    }

    public boolean radiantWon() {
        return radiantWon;
    }

    public long getPlayerSlot() {
        return playerSlot;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public int getLobbyType() {
        return lobbyType;
    }

    public int getGameMode() {
        return gameMode;
    }

    public long getDuration() {
        return duration;
    }

    public long getStartTime() {
        return startTime;
    }
}
