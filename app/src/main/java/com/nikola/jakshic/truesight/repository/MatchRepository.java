package com.nikola.jakshic.truesight.repository;

import android.arch.lifecycle.MutableLiveData;

import com.nikola.jakshic.truesight.data.remote.OpenDotaService;
import com.nikola.jakshic.truesight.model.match.Match;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MatchRepository {

    private OpenDotaService service;

    @Inject
    public MatchRepository(OpenDotaService service) {
        this.service = service;
    }

    public void fetchMatches(MutableLiveData<List<Match>> list, MutableLiveData<Boolean> loading, long id) {
        loading.setValue(true);
        service.getMatches(id).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                list.setValue(response.body());
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }
}