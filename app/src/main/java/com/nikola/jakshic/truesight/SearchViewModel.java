package com.nikola.jakshic.truesight;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.nikola.jakshic.truesight.model.Player;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<Player>> list = new MutableLiveData<>();
    private SearchRepository searchRepository;
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public SearchViewModel(){
        searchRepository = new SearchRepository();
        loading.setValue(false);
    }

    public LiveData<List<Player>> getPlayers() {
        return list;
    }

    public void fetchPlayers(String name) {
        searchRepository.fetchPlayers(list, loading, name);
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }

    public void clearList() {
        list.setValue(new ArrayList<>());
    }
}