
package com.nikola.jakshic.dagger.model.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TowerDamage {

    @SerializedName("raw")
    @Expose
    public long raw;
    @SerializedName("pct")
    @Expose
    public double pct;

}
