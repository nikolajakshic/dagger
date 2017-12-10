package com.nikola.jakshic.truesight.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Player implements Parcelable {

    @SerializedName("account_id")
    private long id;
    @SerializedName("personaname")
    private String name;
    @SerializedName("avatarfull")
    private String avatarUrl;

    public Player() {
    }

    protected Player(Parcel in) {
        id = in.readLong();
        name = in.readString();
        avatarUrl = in.readString();
    }


    public long getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setData(long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.avatarUrl = url;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
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
