package com.nikola.jakshic.dagger.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "peers", primaryKeys = arrayOf("account_id", "peer_id"))
data class Peer(
        @ColumnInfo(name = "account_id") var accountId: Long,
        @ColumnInfo(name = "peer_id") @SerializedName("account_id") @Expose var peerId: Long,
        @ColumnInfo(name = "persona_name") @SerializedName("personaname") @Expose var personaname: String?,
        @ColumnInfo(name = "avatar_url") @SerializedName("avatarfull") @Expose var avatarfull: String?,
        @ColumnInfo(name = "games") @SerializedName("with_games") @Expose var withGames: Int,
        @ColumnInfo(name = "wins") @SerializedName("with_win") @Expose var withWin: Int)