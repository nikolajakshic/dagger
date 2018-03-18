package com.nikola.jakshic.dagger.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Peer {

    @SerializedName("account_id") @Expose private long accountId;
    @SerializedName("with_win") @Expose private long withWin;
    @SerializedName("with_games") @Expose private long withGames;
    @SerializedName("personaname") @Expose private String personaname;
    @SerializedName("avatarfull") @Expose private String avatarfull;

    public long getAccountId() {
        return accountId;
    }

    public long getWithWin() {
        return withWin;
    }

    public long getWithGames() {
        return withGames;
    }

    public String getPersonaname() {
        return personaname;
    }

    public String getAvatarfull() {
        return avatarfull;
    }
}