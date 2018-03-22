package com.nikola.jakshic.dagger.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.model.Peer;
import com.nikola.jakshic.dagger.repository.PeerRepository;

import javax.inject.Inject;

public class PeerViewModel extends ViewModel {

    private LiveData<PagedList<Peer>> list;
    private PeerRepository repository;
    private MutableLiveData<Status> status;
    private boolean initialFetch;

    @Inject
    public PeerViewModel(PeerRepository repository) {
        this.repository = repository;
        status = new MutableLiveData<>();
        status.setValue(Status.LOADING);
    }

    public void initialFetch(long id) {
        if (!initialFetch) {
            list = repository.fetchByGames(id);
            repository.fetchPeers(status, id);
            initialFetch = true;
        }
    }

    // Get list from DB sorted by games
    public void sortByGames(long id) {
        list = repository.fetchByGames(id);
    }

    // Get list from DB sorted by win rate
    public void sortByWinrate(long id) {
       list = repository.fetchByWinrate(id);
    }

    // Fetch peers from network and store them into DB
    public void fetchPeers(long id) {
        repository.fetchPeers(status, id);
    }

    // Expose peer data from DB
    public LiveData<PagedList<Peer>> getPeers() {
        return list;
    }

    // Expose network status
    public MutableLiveData<Status> getStatus() {
        return status;
    }
}