package com.nikola.jakshic.truesight;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PlayerViewModel extends ViewModel {

    private MutableLiveData<List<Player>> list;
    private PlayerRepository repository;
    private MutableLiveData<Boolean> loading;

    @Inject
    public PlayerViewModel(PlayerRepository repository) {
        this.repository = repository;
        list = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        loading.setValue(false);
    }

    public MutableLiveData<List<Player>> getPlayers() {
        return list;
    }

    public void fetchPlayers(String name) {
        repository.fetchPlayers(list, loading, name);
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }

    public void clearList() {
        list.setValue(new ArrayList<>());
    }
}