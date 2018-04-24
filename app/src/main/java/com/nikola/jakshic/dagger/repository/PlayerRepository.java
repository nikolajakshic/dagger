package com.nikola.jakshic.dagger.repository;

import android.arch.lifecycle.MutableLiveData;

import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.model.Player;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class PlayerRepository {

    private OpenDotaService service;
    private Disposable disposable;

    @Inject
    public PlayerRepository(OpenDotaService service) {
        this.service = service;
    }

    public Disposable fetchPlayers(MutableLiveData<List<Player>> list, MutableLiveData<Status> loading, String name) {
        // Users can hit the search button multiple times
        // So we need to cancel previous call
        if (disposable != null)
            disposable.dispose();

        loading.setValue(Status.LOADING);
        disposable = service.searchPlayers(name)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        response -> {
                            list.postValue(response);
                            loading.postValue(Status.SUCCESS);
                        },
                        error -> loading.postValue(Status.ERROR));
        return disposable;
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