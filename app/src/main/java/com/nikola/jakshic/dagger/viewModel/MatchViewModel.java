package com.nikola.jakshic.dagger.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.model.match.Match;
import com.nikola.jakshic.dagger.repository.MatchRepository;

import javax.inject.Inject;

public class MatchViewModel extends ViewModel {

    private LiveData<PagedList<Match>> list;
    private MatchRepository repository;
    private MutableLiveData<Status> status;
    private boolean initialFetch;

    @Inject
    public MatchViewModel(MatchRepository repository) {
        this.repository = repository;
        status = new MutableLiveData<>();
        status.setValue(Status.LOADING);
    }

    public void initialFetch(long id) {
        if (!initialFetch) {
            list = repository.getMatches(status,id);
            repository.refreshMatches(status, id);
            initialFetch = true;
        }
    }

    public void fetchHeroes(long id) {
        repository.refreshMatches(status, id);
    }

    public LiveData<PagedList<Match>> getMatches() {
        return list;
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }
}