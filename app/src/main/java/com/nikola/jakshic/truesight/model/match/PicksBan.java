
package com.nikola.jakshic.truesight.model.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PicksBan {

    @SerializedName("is_pick")
    @Expose
    public boolean isPick;
    @SerializedName("hero_id")
    @Expose
    public long heroId;
    @SerializedName("team")
    @Expose
    public long team;
    @SerializedName("order")
    @Expose
    public long order;
    @SerializedName("ord")
    @Expose
    public long ord;
    @SerializedName("match_id")
    @Expose
    public long matchId;

}
