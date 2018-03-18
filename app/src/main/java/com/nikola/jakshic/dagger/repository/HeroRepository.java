package com.nikola.jakshic.dagger.repository;

import android.arch.lifecycle.MutableLiveData;

import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.model.Hero;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class HeroRepository {

    private OpenDotaService service;

    @Inject
    public HeroRepository(OpenDotaService service) {
        this.service = service;
    }

    public void fetchHeroes(MutableLiveData<List<Hero>> list, MutableLiveData<Boolean> loading, long id) {
        loading.setValue(true);
        service.getHeroes(id).enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {
                list.setValue(response.body());
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Hero>> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }
}