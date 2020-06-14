package com.nikola.jakshic.dagger.profile.peers

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.nikola.jakshic.dagger.common.sqldelight.Peers
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "peers", primaryKeys = arrayOf("account_id", "peer_id"))
@JsonClass(generateAdapter = true)
data class PeerJson(
    @ColumnInfo(name = "account_id") var accountId: Long = 0,
    @ColumnInfo(name = "peer_id") @Json(name = "account_id") var peerId: Long,
    @ColumnInfo(name = "persona_name") @Json(name = "personaname") var personaname: String?,
    @ColumnInfo(name = "avatar_url") @Json(name = "avatarfull") var avatarfull: String?,
    @ColumnInfo(name = "games") @Json(name = "with_games") var withGames: Int,
    @ColumnInfo(name = "wins") @Json(name = "with_win") var withWin: Int
)

data class PeerUI(
    val peerId: Long,
    val personaname: String?,
    val avatarfull: String?,
    val withGames: Int,
    val withWin: Int
)

fun PeerJson.mapToDb(): Peers {
    return Peers(
        account_id = this.accountId,
        peer_id = this.peerId,
        persona_name = this.personaname,
        avatar_url = this.avatarfull,
        games = this.withGames.toLong(),
        wins = this.withWin.toLong()
    )
}

fun mapToUi(
    account_id: Long,
    peer_id: Long,
    persona_name: String?,
    avatar_url: String?,
    games: Long,
    wins: Long
): PeerUI {
    return PeerUI(
        peerId = peer_id,
        personaname = persona_name,
        avatarfull = avatar_url,
        withGames = games.toInt(),
        withWin = wins.toInt()
    )
}