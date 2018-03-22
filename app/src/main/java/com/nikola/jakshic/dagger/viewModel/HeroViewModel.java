package com.nikola.jakshic.dagger.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.model.Hero;
import com.nikola.jakshic.dagger.repository.HeroRepository;

import javax.inject.Inject;

public class HeroViewModel extends ViewModel {

    private static final String LOG_TAG = HeroViewModel.class.getSimpleName();

    private LiveData<PagedList<Hero>> list;
    private HeroRepository repository;
    private MutableLiveData<Status> status;
    private boolean initialFetch;

    @Inject
    public HeroViewModel(HeroRepository repository) {
        this.repository = repository;
        status = new MutableLiveData<>();
        status.setValue(Status.LOADING);
    }

    public void initialFetch(long id) {
        if (!initialFetch) {
            list = repository.fetchByGames(id);
            repository.fetchHeroes(status, id);
            initialFetch = true;
        }
    }

    public void fetchHeroes(long id) {
        repository.fetchHeroes(status, id);
    }

    public void sortByGames(long id){
        list = repository.fetchByGames(id);
    }

    public void sortByWinrate(long id){
        list = repository.fetchByWinrate(id);
    }

    public void sortByWins(long id){
        list = repository.fetchByWins(id);
    }

    public void sortByLosses(long id){
        list = repository.fetchByLosses(id);
    }

    public LiveData<PagedList<Hero>> getHeroes() {
        return list;
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }
}