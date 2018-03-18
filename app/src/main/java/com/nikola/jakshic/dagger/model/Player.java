package com.nikola.jakshic.dagger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nikola.jakshic.dagger.model.match.Benchmarks;
import com.nikola.jakshic.dagger.model.match.GoldReasons;
import com.nikola.jakshic.dagger.model.match.XpReasons;

import java.util.List;

@Entity
public class Player implements Parcelable {

    @Expose @SerializedName("account_id") private long id;
    @Expose @SerializedName("personaname") private String personaName;
    @Expose @SerializedName("avatarfull") private String avatarUrl;
    @Expose @Ignore @SerializedName("win") private long wins;
    @Expose @Ignore @SerializedName("lose") private long losses;
    @PrimaryKey(autoGenerate = true) private long count;

    @Ignore private boolean expanded;

    @SerializedName("player_slot") @Ignore @Expose public long playerSlot;
    @SerializedName("assists") @Ignore @Expose public long assists;
    @SerializedName("backpack_0") @Ignore @Expose public long backpack0;
    @SerializedName("backpack_1") @Ignore @Expose public long backpack1;
    @SerializedName("backpack_2") @Ignore @Expose public long backpack2;
    @SerializedName("camps_stacked") @Ignore @Expose public long campsStacked;
    @SerializedName("creeps_stacked") @Ignore @Expose public long creepsStacked;
    @SerializedName("deaths") @Ignore @Expose public long deaths;
    @SerializedName("denies") @Ignore @Expose public long denies;
    @SerializedName("gold_per_min") @Ignore @Expose public long goldPerMin;
    @SerializedName("gold_reasons") @Ignore @Expose public GoldReasons goldReasons;
    @SerializedName("gold_t") @Ignore @Expose public List<Long> goldT = null;
    @SerializedName("dn_t") @Ignore @Expose public List<Long> dnT = null;
    @SerializedName("hero_damage") @Ignore @Expose public long heroDamage;
    @SerializedName("hero_healing") @Ignore @Expose public long heroHealing;
    @SerializedName("hero_id") @Ignore @Expose public long heroId;
    @SerializedName("item_0") @Ignore @Expose public long item0;
    @SerializedName("item_1") @Ignore @Expose public long item1;
    @SerializedName("item_2") @Ignore @Expose public long item2;
    @SerializedName("item_3") @Ignore @Expose public long item3;
    @SerializedName("item_4") @Ignore @Expose public long item4;
    @SerializedName("item_5") @Ignore @Expose public long item5;
    @SerializedName("kills") @Ignore @Expose public long kills;
    @SerializedName("last_hits") @Ignore @Expose public long lastHits;
    @SerializedName("level") @Ignore @Expose public long level;
    @SerializedName("lh_t") @Ignore @Expose public List<Long> lhT = null;
    @SerializedName("stuns") @Ignore @Expose public double stuns;
    @SerializedName("tower_damage") @Ignore @Expose public long towerDamage;
    @SerializedName("xp_per_min") @Ignore @Expose public long xpPerMin;
    @SerializedName("xp_reasons") @Ignore @Expose public XpReasons xpReasons;
    @SerializedName("xp_t") @Ignore @Expose public List<Long> xpT = null;
    @SerializedName("name") @Ignore @Expose public String name;
    @SerializedName("radiant_win") @Ignore @Expose public boolean radiantWin;
    @SerializedName("start_time") @Ignore @Expose public long startTime;
    @SerializedName("duration") @Ignore @Expose public long duration;
    @SerializedName("isRadiant") @Ignore @Expose public boolean isRadiant;
    @SerializedName("total_gold") @Ignore @Expose public long totalGold;
    @SerializedName("total_xp") @Ignore @Expose public long totalXp;
    @SerializedName("kills_per_min") @Ignore @Expose public double killsPerMin;
    @SerializedName("purchase_ward_observer") @Ignore @Expose public long purchaseWardObserver;
    @SerializedName("purchase_ward_sentry") @Ignore @Expose public long purchaseWardSentry;
    @SerializedName("purchase_tpscroll") @Ignore @Expose public long purchaseTpscroll;
    @SerializedName("purchase_gem") @Ignore @Expose public long purchaseGem;
    @SerializedName("benchmarks") @Ignore @Expose public Benchmarks benchmarks;

    public Player() {
    }

    protected Player(Parcel in) {
        id = in.readLong();
        personaName = in.readString();
        avatarUrl = in.readString();
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public long getPlayerSlot() {
        return playerSlot;
    }

    public long getAssists() {
        return assists;
    }

    public long getBackpack0() {
        return backpack0;
    }

    public long getBackpack1() {
        return backpack1;
    }

    public long getBackpack2() {
        return backpack2;
    }

    public long getCampsStacked() {
        return campsStacked;
    }

    public long getCreepsStacked() {
        return creepsStacked;
    }

    public long getDeaths() {
        return deaths;
    }

    public long getDenies() {
        return denies;
    }

    public long getGoldPerMin() {
        return goldPerMin;
    }

    public GoldReasons getGoldReasons() {
        return goldReasons;
    }

    public List<Long> getGoldT() {
        return goldT;
    }

    public List<Long> getDnT() {
        return dnT;
    }

    public long getHeroDamage() {
        return heroDamage;
    }

    public long getHeroHealing() {
        return heroHealing;
    }

    public long getHeroId() {
        return heroId;
    }

    public long getItem0() {
        return item0;
    }

    public long getItem1() {
        return item1;
    }

    public long getItem2() {
        return item2;
    }

    public long getItem3() {
        return item3;
    }

    public long getItem4() {
        return item4;
    }

    public long getItem5() {
        return item5;
    }

    public long getKills() {
        return kills;
    }

    public long getLastHits() {
        return lastHits;
    }

    public long getLevel() {
        return level;
    }

    public List<Long> getLhT() {
        return lhT;
    }

    public double getStuns() {
        return stuns;
    }

    public long getTowerDamage() {
        return towerDamage;
    }

    public long getXpPerMin() {
        return xpPerMin;
    }

    public XpReasons getXpReasons() {
        return xpReasons;
    }

    public List<Long> getXpT() {
        return xpT;
    }

    public String getName() {
        return name;
    }

    public boolean isRadiantWin() {
        return radiantWin;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isRadiant() {
        return isRadiant;
    }

    public long getTotalGold() {
        return totalGold;
    }

    public long getTotalXp() {
        return totalXp;
    }

    public double getKillsPerMin() {
        return killsPerMin;
    }

    public long getPurchaseWardObserver() {
        return purchaseWardObserver;
    }

    public long getPurchaseWardSentry() {
        return purchaseWardSentry;
    }

    public long getPurchaseTpscroll() {
        return purchaseTpscroll;
    }

    public long getPurchaseGem() {
        return purchaseGem;
    }

    public Benchmarks getBenchmarks() {
        return benchmarks;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPersonaName(String name) {
        this.personaName = name;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getWins() {
        return wins;
    }

    public void setWins(long wins) {
        this.wins = wins;
    }

    public long getLosses() {
        return losses;
    }

    public void setLosses(long losses) {
        this.losses = losses;
    }

    public long getId() {
        return id;
    }

    public String getPersonaName() {
        return personaName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public long getCount() {
        return count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(personaName);
        dest.writeString(avatarUrl);
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}