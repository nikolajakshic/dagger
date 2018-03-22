package com.nikola.jakshic.dagger.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hero {

    @SerializedName("hero_id") @Expose private long id;
    @SerializedName("games") @Expose private int gamesPlayed;
    @SerializedName("win") @Expose private int gamesWon;

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