package com.nikola.jakshic.dagger.data.remote

import com.nikola.jakshic.dagger.model.*
import com.nikola.jakshic.dagger.model.match.Match
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenDotaService {

    companion object {
        const val BASE_URL = "https://api.opendota.com/api/"
    }

    @GET("players/{account_id}")
    fun getPlayerProfile(@Path("account_id") id: Long): Observable<_Profile>

    @GET("search")
    fun searchPlayers(@Query("q") name: String): Call<List<Player>>

    @GET("players/{account_id}/wl")
    fun getPlayerWinLoss(@Path("account_id") playerId: Long): Call<Player>

    @GET("proMatches")
    fun getCompetitiveMatches(): Observable<List<Competitive>>

    @GET("matches/{match_id}/")
    fun getMatch(@Path("match_id") matchId: Long): Call<Match>

    @GET("players/{account_id}/matches")
    fun getMatches(
            @Path("account_id") playerId: Long,
            @Query("limit") limit: Long,
            @Query("offset") offset: Long): Call<List<Match>>

    @GET("players/{account_id}/heroes")
    fun getHeroes(@Path("account_id") playerId: Long): Call<List<Hero>>

    @GET("players/{account_id}/peers")
    fun getPeers(@Path("account_id") playerId: Long): Call<List<Peer>>

    @GET("https://www.dota2.com/webapi/ILeaderboard/GetDivisionLeaderboard/v0001")
    fun getLeaderboard(@Query("division") region: String): Observable<_Leaderboard>
}