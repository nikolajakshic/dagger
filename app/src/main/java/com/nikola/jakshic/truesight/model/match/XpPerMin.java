
package com.nikola.jakshic.truesight.model.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class XpPerMin {

    @SerializedName("raw")
    @Expose
    public long raw;
    @SerializedName("pct")
    @Expose
    public double pct;

}
