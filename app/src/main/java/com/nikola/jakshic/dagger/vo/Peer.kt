package com.nikola.jakshic.dagger.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "peers", primaryKeys = arrayOf("account_id", "peer_id"))
@JsonClass(generateAdapter = true)
data class Peer(
        @ColumnInfo(name = "account_id") var accountId: Long = 0,
        @ColumnInfo(name = "peer_id") @Json(name = "account_id") var peerId: Long,
        @ColumnInfo(name = "persona_name") @Json(name = "personaname") var personaname: String?,
        @ColumnInfo(name = "avatar_url") @Json(name = "avatarfull") var avatarfull: String?,
        @ColumnInfo(name = "games") @Json(name = "with_games") var withGames: Int,
        @ColumnInfo(name = "wins") @Json(name = "with_win") var withWin: Int)