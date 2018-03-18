
package com.nikola.jakshic.dagger.model.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class League {

    @SerializedName("leagueid")
    @Expose
    public long leagueid;
    @SerializedName("ticket")
    @Expose
    public String ticket;
    @SerializedName("banner")
    @Expose
    public String banner;
    @SerializedName("tier")
    @Expose
    public String tier;
    @SerializedName("name")
    @Expose
    public String name;

}
