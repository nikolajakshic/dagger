package com.nikola.jakshic.dagger.profile.peers

import com.nikola.jakshic.dagger.common.sqldelight.Peers
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PeerJson(
    @Json(name = "account_id") val peerId: Long,
    @Json(name = "personaname") val personaname: String?,
    @Json(name = "avatarfull") val avatarfull: String?,
    @Json(name = "with_games") val withGames: Long,
    @Json(name = "with_win") val withWin: Long,
)

data class PeerUI(
    val peerId: Long,
    val personaname: String?,
    val avatarfull: String?,
    val withGames: Long,
    val withWin: Long,
)

fun PeerJson.mapToDb(accountId: Long): Peers {
    return Peers(
        account_id = accountId,
        peer_id = this.peerId,
        persona_name = this.personaname,
        avatar_url = this.avatarfull,
        games = this.withGames,
        wins = this.withWin,
    )
}

fun List<Peers>.mapToUi(): List<PeerUI> {
    val list = mutableListOf<PeerUI>()
    for (item in this) {
        list.add(
            PeerUI(
                peerId = item.peer_id,
                personaname = item.persona_name,
                avatarfull = item.avatar_url,
                withGames = item.games,
                withWin = item.wins,
            ),
        )
    }
    return list
}
