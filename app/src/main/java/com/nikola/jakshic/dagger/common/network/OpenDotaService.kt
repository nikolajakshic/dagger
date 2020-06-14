package com.nikola.jakshic.dagger.common.network

import com.nikola.jakshic.dagger.competitive.CompetitiveJson
import com.nikola.jakshic.dagger.leaderboard._Leaderboard
import com.nikola.jakshic.dagger.matchstats.MatchStats
import com.nikola.jakshic.dagger.profile.PlayerJson
import com.nikola.jakshic.dagger.profile._Player
import com.nikola.jakshic.dagger.profile.heroes.Hero
import com.nikola.jakshic.dagger.profile.matches.MatchJson
import com.nikola.jakshic.dagger.profile.peers.Peer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenDotaService {

    companion object {
        const val BASE_URL = "https://api.opendota.com/api/"
    }

    @GET("search")
    suspend fun searchPlayers(@Query("q") name: String): List<PlayerJson>

    @GET("players/{account_id}")
    suspend fun getPlayerProfile(@Path("account_id") id: Long): _Player

    @GET("players/{account_id}/wl")
    suspend fun getPlayerWinLoss(@Path("account_id") playerId: Long): PlayerJson

    @GET("players/{account_id}/matches?significant=0")
    suspend fun getMatches(
        @Path("account_id") playerId: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<MatchJson>

    @GET("players/{account_id}/matches?significant=0")
    suspend fun getMatchesByHero(
        @Path("account_id") playerId: Long,
        @Query("hero_id") heroId: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<MatchJson>

    @GET("players/{account_id}/heroes")
    suspend fun getHeroes(@Path("account_id") playerId: Long): List<Hero>

    @GET("players/{account_id}/peers")
    suspend fun getPeers(@Path("account_id") playerId: Long): List<Peer>

    @GET("matches/{match_id}/")
    suspend fun getMatch(@Path("match_id") matchId: Long): MatchStats

    @GET("proMatches")
    suspend fun getCompetitiveMatches(): List<CompetitiveJson>

    @GET("https://www.dota2.com/webapi/ILeaderboard/GetDivisionLeaderboard/v0001")
    suspend fun getLeaderboard(@Query("division") region: String): _Leaderboard
}