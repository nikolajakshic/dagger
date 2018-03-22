package com.nikola.jakshic.dagger.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "peers", primaryKeys = {"account_id", "peer_id"})
public class Peer {

    @ColumnInfo(name = "account_id") private long accountId;
    @ColumnInfo(name = "peer_id") @SerializedName("account_id") @Expose private long peerId;
    @ColumnInfo(name = "persona_name") @SerializedName("personaname") @Expose private String personaname;
    @ColumnInfo(name = "avatar_url") @SerializedName("avatarfull") @Expose private String avatarfull;
    @ColumnInfo(name = "games") @SerializedName("with_games") @Expose private long withGames;
    @ColumnInfo(name = "wins") @SerializedName("with_win") @Expose private long withWin;

    public long getPeerId() {
        return peerId;
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

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }

    public void setPersonaname(String personaname) {
        this.personaname = personaname;
    }

    public void setAvatarfull(String avatarfull) {
        this.avatarfull = avatarfull;
    }

    public void setWithGames(long withGames) {
        this.withGames = withGames;
    }

    public void setWithWin(long withWin) {
        this.withWin = withWin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peer peer = (Peer) o;

        if (accountId != peer.accountId) return false;
        if (peerId != peer.peerId) return false;
        if (withGames != peer.withGames) return false;
        if (withWin != peer.withWin) return false;
        if (personaname != null ? !personaname.equals(peer.personaname) : peer.personaname != null)
            return false;
        return avatarfull != null ? avatarfull.equals(peer.avatarfull) : peer.avatarfull == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (accountId ^ (accountId >>> 32));
        result = 31 * result + (int) (peerId ^ (peerId >>> 32));
        result = 31 * result + (personaname != null ? personaname.hashCode() : 0);
        result = 31 * result + (avatarfull != null ? avatarfull.hashCode() : 0);
        result = 31 * result + (int) (withGames ^ (withGames >>> 32));
        result = 31 * result + (int) (withWin ^ (withWin >>> 32));
        return result;
    }
}