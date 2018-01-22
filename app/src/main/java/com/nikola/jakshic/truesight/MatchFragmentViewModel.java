package com.nikola.jakshic.truesight;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.truesight.model.Match;

import java.util.List;

import javax.inject.Inject;

public class MatchFragmentViewModel extends ViewModel{

    private MutableLiveData<List<Match>> list = new MutableLiveData<>();
    private MatchRepository heroRepository;
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public MatchFragmentViewModel() {
        heroRepository = new MatchRepository();
        loading.setValue(false);
    }

    public void fetchHeroes(long id) {
        heroRepository.fetchMatches(list, loading, id);
    }

    public MutableLiveData<List<Match>> getHeroes() {
        return list;
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }
}
