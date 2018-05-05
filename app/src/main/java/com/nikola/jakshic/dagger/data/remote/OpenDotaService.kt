package com.nikola.jakshic.dagger.data.remote

import com.nikola.jakshic.dagger.vo.*
import com.nikola.jakshic.dagger.vo.Match
import com.nikola.jakshic.dagger.vo.MatchStats
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenDotaService {

    companion object {
        const val BASE_URL = "https://api.opendota.com/api/"
    }

    @GET("search")
    fun searchPlayers(@Query("q") name: String): Observable<List<Player>>

    @GET("players/{account_id}")
    fun getPlayerProfile(@Path("account_id") id: Long): Observable<_Player>

    @GET("players/{account_id}/wl")
    fun getPlayerWinLoss(@Path("account_id") playerId: Long): Observable<Player>

    @GET("players/{account_id}/matches")
    fun getMatches(
            @Path("account_id") playerId: Long,
            @Query("limit") limit: Long,
            @Query("offset") offset: Long): Observable<List<Match>>

    @GET("players/{account_id}/heroes")
    fun getHeroes(@Path("account_id") playerId: Long): Observable<List<Hero>>

    @GET("players/{account_id}/peers")
    fun getPeers(@Path("account_id") playerId: Long): Observable<List<Peer>>

    @GET("matches/{match_id}/")
    fun getMatch(@Path("match_id") matchId: Long): Call<MatchStats>

    @GET("proMatches")
    fun getCompetitiveMatches(): Observable<List<Competitive>>

    @GET("https://www.dota2.com/webapi/ILeaderboard/GetDivisionLeaderboard/v0001")
    fun getLeaderboard(@Query("division") region: String): Observable<_Leaderboard>
}