package com.nikola.jakshic.dagger.common.network

import com.nikola.jakshic.dagger.competitive.CompetitiveJson
import com.nikola.jakshic.dagger.leaderboard.LeaderboardUrlJson
import com.nikola.jakshic.dagger.leaderboard._Leaderboard
import com.nikola.jakshic.dagger.matchstats.MatchStatsJson
import com.nikola.jakshic.dagger.profile.PlayerJson
import com.nikola.jakshic.dagger.profile.PlayerWinLossJson
import com.nikola.jakshic.dagger.profile._PlayerJson
import com.nikola.jakshic.dagger.profile.heroes.HeroJson
import com.nikola.jakshic.dagger.profile.matches.MatchJson
import com.nikola.jakshic.dagger.profile.peers.PeerJson
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface OpenDotaService {

    companion object {
        const val BASE_URL = "https://api.opendota.com/api/"
    }

    @GET("search")
    suspend fun searchPlayers(@Query("q") name: String): List<PlayerJson>

    @GET("players/{account_id}")
    suspend fun getPlayerProfile(@Path("account_id") id: Long): _PlayerJson

    @GET("players/{account_id}/wl")
    suspend fun getPlayerWinLoss(@Path("account_id") playerId: Long): PlayerWinLossJson

    @GET("players/{account_id}/matches?significant=0")
    suspend fun getMatches(
        @Path("account_id") playerId: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<MatchJson>

    @GET("players/{account_id}/matches?significant=0")
    suspend fun getMatchesByHero(
        @Path("account_id") playerId: Long,
        @Query("hero_id") heroId: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<MatchJson>

    @GET("players/{account_id}/heroes")
    suspend fun getHeroes(@Path("account_id") playerId: Long): List<HeroJson>

    @GET("players/{account_id}/peers")
    suspend fun getPeers(@Path("account_id") playerId: Long): List<PeerJson>

    @GET("matches/{match_id}/")
    suspend fun getMatch(@Path("match_id") matchId: Long): MatchStatsJson

    @GET("proMatches")
    suspend fun getCompetitiveMatches(): List<CompetitiveJson>

    @GET("https://nikolajakshic.github.io/dagger/remote-config.json")
    suspend fun getLeaderboardUrl(): LeaderboardUrlJson

    @GET
    suspend fun getLeaderboard(@Url url: String, @Query("division") region: String): _Leaderboard
}
