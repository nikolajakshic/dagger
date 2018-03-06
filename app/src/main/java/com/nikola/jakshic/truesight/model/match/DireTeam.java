
package com.nikola.jakshic.truesight.model.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DireTeam {

    @SerializedName("team_id")
    @Expose
    public long teamId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("tag")
    @Expose
    public String tag;
    @SerializedName("logo_url")
    @Expose
    public String logoUrl;

}
