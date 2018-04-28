package com.nikola.jakshic.dagger.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.dagger.model.match.MatchStats;
import com.nikola.jakshic.dagger.repository.MatchRepository;

import javax.inject.Inject;

public class MatchDetailViewModel extends ViewModel {

    private MutableLiveData<MatchStats> match;
    private MatchRepository repository;
    private MutableLiveData<Boolean> loading;
    private boolean initialFetch;

    @Inject
    public MatchDetailViewModel(MatchRepository repository) {
        this.repository = repository;
        match = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        loading.setValue(false);
    }

    public void initialFetch(long matchId) {
        if (!initialFetch) {
            repository.fetchMatchData(match, loading, matchId);
            initialFetch = true;
        }
    }

    public void fetchMatchData(long matchId) {
        repository.fetchMatchData(match, loading, matchId);
    }

    public MutableLiveData<MatchStats> getMatchData() {
        return match;
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }
}