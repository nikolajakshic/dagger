package com.nikola.jakshic.dagger.data.remote

import com.nikola.jakshic.dagger.vo.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenDotaService {

    companion object {
        const val BASE_URL = "https://api.opendota.com/api/"
    }

    @GET("search")
    suspend fun searchPlayers(@Query("q") name: String): List<Player>

    @GET("players/{account_id}")
    suspend fun getPlayerProfile(@Path("account_id") id: Long): _Player

    @GET("players/{account_id}/wl")
    suspend fun getPlayerWinLoss(@Path("account_id") playerId: Long): Player

    @GET("players/{account_id}/matches?significant=0")
    suspend fun getMatches(
            @Path("account_id") playerId: Long,
            @Query("limit") limit: Int,
            @Query("offset") offset: Int): List<Match>

    @GET("players/{account_id}/matches?significant=0")
    suspend fun getMatchesByHero(
            @Path("account_id") playerId: Long,
            @Query("hero_id") heroId: Int,
            @Query("limit") limit: Int,
            @Query("offset") offset: Int): List<Match>

    @GET("players/{account_id}/heroes")
    suspend fun getHeroes(@Path("account_id") playerId: Long): List<Hero>

    @GET("players/{account_id}/peers")
    suspend fun getPeers(@Path("account_id") playerId: Long): List<Peer>

    @GET("matches/{match_id}/")
    suspend fun getMatch(@Path("match_id") matchId: Long): MatchStats

    @GET("proMatches")
    suspend fun getCompetitiveMatches(): List<Competitive>

    @GET("https://www.dota2.com/webapi/ILeaderboard/GetDivisionLeaderboard/v0001")
    suspend fun getLeaderboard(@Query("division") region: String): _Leaderboard
}