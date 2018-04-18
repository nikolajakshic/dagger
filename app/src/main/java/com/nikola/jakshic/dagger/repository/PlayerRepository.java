package com.nikola.jakshic.dagger.repository;

import android.arch.lifecycle.MutableLiveData;

import com.crashlytics.android.Crashlytics;
import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.model.Player;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class PlayerRepository {

    private OpenDotaService service;
    private Call<List<Player>> call;

    @Inject
    public PlayerRepository(OpenDotaService service) {
        this.service = service;
    }

    public void fetchPlayers(MutableLiveData<List<Player>> list, MutableLiveData<Boolean> loading, String name) {

        // Cancel previous call
        if (call != null)
            call.cancel();

        call = service.searchPlayers(name);
        loading.setValue(true);
        call.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                list.setValue(response.body());
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                if (!call.isCanceled()) loading.setValue(false);
            }
        });
    }

    public void fetchPlayerWinLoss(MutableLiveData<Player> player, long id) {
        service.getPlayerWinLoss(id).enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                player.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Player> call, Throwable t) {
            }
        });
    }
}