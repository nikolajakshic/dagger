package com.nikola.jakshic.truesight.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Peer {

    @SerializedName("account_id") @Expose private int accountId;
    @SerializedName("with_win") @Expose private int withWin;
    @SerializedName("with_games") @Expose private int withGames;
    @SerializedName("personaname") @Expose private String personaname;
    @SerializedName("avatarfull") @Expose private String avatarfull;

    public int getAccountId() {
        return accountId;
    }

    public int getWithWin() {
        return withWin;
    }

    public int getWithGames() {
        return withGames;
    }

    public String getPersonaname() {
        return personaname;
    }

    public String getAvatarfull() {
        return avatarfull;
    }
}