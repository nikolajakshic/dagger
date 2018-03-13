package com.nikola.jakshic.truesight.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.truesight.model.Competitive;
import com.nikola.jakshic.truesight.repository.MatchRepository;

import java.util.List;

import javax.inject.Inject;

public class CompetitiveViewModel extends ViewModel {

    private MutableLiveData<List<Competitive>> list;
    private MatchRepository repository;
    private MutableLiveData<Boolean> loading;
    private boolean initialFetch;

    @Inject
    public CompetitiveViewModel(MatchRepository repository) {
        this.repository = repository;
        list = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        loading.setValue(false);
    }

    public void initialFetch() {
        if (!initialFetch) {
            repository.fetchCompetitiveMatches(list, loading);
            initialFetch = true;
        }
    }

    public void fetchCompetitiveMatches( ) {
        repository.fetchCompetitiveMatches(list, loading);
    }

    public MutableLiveData<List<Competitive>> getCompetitive() {
        return list;
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }
}