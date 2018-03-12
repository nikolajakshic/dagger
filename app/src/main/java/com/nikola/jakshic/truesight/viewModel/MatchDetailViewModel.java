package com.nikola.jakshic.truesight.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.truesight.model.match.Match;
import com.nikola.jakshic.truesight.repository.MatchRepository;

import javax.inject.Inject;

public class MatchDetailViewModel extends ViewModel {

    private MutableLiveData<Match> match;
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

    public MutableLiveData<Match> getMatchData() {
        return match;
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }
}