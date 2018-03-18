package com.nikola.jakshic.dagger.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.model.Competitive;
import com.nikola.jakshic.dagger.repository.MatchRepository;

import javax.inject.Inject;

public class CompetitiveViewModel extends ViewModel {

    private LiveData<PagedList<Competitive>> list;
    private MatchRepository repository;
    private MutableLiveData<Status> status;
    private boolean initialFetch;

    @Inject
    public CompetitiveViewModel(MatchRepository repository) {
        this.repository = repository;
        list = new MutableLiveData<>();
        status = new MutableLiveData<>();
        status.setValue(Status.INITIAL_LOADING);
        list = repository.getCompetitiveMatches();
    }

    public void initialFetch() {
        if (!initialFetch) {
            repository.fetchCompetitiveMatches(status);
            initialFetch = true;
        }
    }

    public void refreshData(){
        status.setValue(Status.LOADING);
        repository.fetchCompetitiveMatches(status);
    }

    public LiveData<PagedList<Competitive>> getCompetitiveMatches() {
        return list;
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }
}