package com.nikola.jakshic.dagger.model;

import com.google.gson.annotations.SerializedName;

public class Hero {

    @SerializedName("hero_id") private long id;
    @SerializedName("games") private int gamesPlayed;
    @SerializedName("win") private int gamesWon;

    public long getID() {
        return id;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }
}