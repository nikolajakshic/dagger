package com.nikola.jakshic.dagger.repository;

import android.arch.lifecycle.MutableLiveData;

import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.model.Peer;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class PeerRepository {

    private OpenDotaService service;

    @Inject
    public PeerRepository(OpenDotaService service) {
        this.service = service;
    }

    public void fetchPeers(MutableLiveData<List<Peer>> list, MutableLiveData<Boolean> loading, long id) {
        loading.setValue(true);
        service.getPeers(id).enqueue(new Callback<List<Peer>>() {
            @Override
            public void onResponse(Call<List<Peer>> call, Response<List<Peer>> response) {
                list.setValue(response.body());
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Peer>> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }
}