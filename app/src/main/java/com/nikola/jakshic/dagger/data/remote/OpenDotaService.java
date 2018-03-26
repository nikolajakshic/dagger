package com.nikola.jakshic.dagger.data.remote;

import com.nikola.jakshic.dagger.model.Hero;
import com.nikola.jakshic.dagger.model.Peer;
import com.nikola.jakshic.dagger.model.Player;
import com.nikola.jakshic.dagger.model.Competitive;
import com.nikola.jakshic.dagger.model.match.Match;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenDotaService {

    String BASE_URL = "https://api.opendota.com/api/";

    @GET("search")
    Call<List<Player>> searchPlayers(@Query("q") String name);

    @GET("players/{account_id}/wl")
    Call<Player> getPlayerWinLoss(@Path("account_id") long playerId);

    @GET("proMatches")
    Call<List<Competitive>> getCompetitiveMatches();

    @GET("matches/{match_id}/")
    Call<Match> getMatch(@Path("match_id") long matchId);

    @GET("players/{account_id}/matches")
    Call<List<Match>> getMatches(
            @Path("account_id") long playerId,
            @Query("limit") long limit,
            @Query("offset") long offset);

    @GET("players/{account_id}/heroes")
    Call<List<Hero>> getHeroes(@Path("account_id") long playerId);

    @GET("players/{account_id}/peers")
    Call<List<Peer>> getPeers(@Path("account_id") long playerId);
}