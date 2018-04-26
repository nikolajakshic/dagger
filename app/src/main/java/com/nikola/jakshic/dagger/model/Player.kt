package com.nikola.jakshic.dagger.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class _Player(
        @SerializedName("profile") @Expose val player: Player)

@Entity(tableName = "players")
data class Player(
        @PrimaryKey @ColumnInfo(name = "account_id") @SerializedName("account_id") @Expose var id: Long,
        @ColumnInfo(name = "name") @SerializedName("name") @Expose var name: String? = null,
        @ColumnInfo(name = "persona_name") @SerializedName("personaname") @Expose var personaName: String?,
        @ColumnInfo(name = "avatar_url") @SerializedName("avatarfull") @Expose var avatarUrl: String?,
        @ColumnInfo(name = "wins") @SerializedName("win") @Expose var wins: Long,
        @ColumnInfo(name = "losses") @SerializedName("lose") @Expose var losses: Long)