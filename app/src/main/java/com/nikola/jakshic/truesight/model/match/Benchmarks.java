
package com.nikola.jakshic.truesight.model.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Benchmarks {

    @SerializedName("gold_per_min")
    @Expose
    public GoldPerMin goldPerMin;
    @SerializedName("xp_per_min")
    @Expose
    public XpPerMin xpPerMin;
    @SerializedName("kills_per_min")
    @Expose
    public KillsPerMin killsPerMin;
    @SerializedName("last_hits_per_min")
    @Expose
    public LastHitsPerMin lastHitsPerMin;
    @SerializedName("hero_damage_per_min")
    @Expose
    public HeroDamagePerMin heroDamagePerMin;
    @SerializedName("hero_healing_per_min")
    @Expose
    public HeroHealingPerMin heroHealingPerMin;
    @SerializedName("tower_damage")
    @Expose
    public TowerDamage towerDamage;

}
