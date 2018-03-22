package com.nikola.jakshic.dagger.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "heroes", primaryKeys = {"account_id", "hero_id"})
public class Hero {

    @ColumnInfo(name = "account_id") private long accountId;
    @ColumnInfo(name = "hero_id") @SerializedName("hero_id") @Expose private long heroId;
    @ColumnInfo(name = "games")@SerializedName("games") @Expose private int gamesPlayed;
    @ColumnInfo(name = "wins") @SerializedName("win") @Expose private int gamesWon;

    public long getHeroId() {
        return heroId;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setHeroId(long heroId) {
        this.heroId = heroId;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hero hero = (Hero) o;

        if (accountId != hero.accountId) return false;
        if (heroId != hero.heroId) return false;
        if (gamesPlayed != hero.gamesPlayed) return false;
        return gamesWon == hero.gamesWon;
    }

    @Override
    public int hashCode() {
        int result = (int) (accountId ^ (accountId >>> 32));
        result = 31 * result + (int) (heroId ^ (heroId >>> 32));
        result = 31 * result + gamesPlayed;
        result = 31 * result + gamesWon;
        return result;
    }
}