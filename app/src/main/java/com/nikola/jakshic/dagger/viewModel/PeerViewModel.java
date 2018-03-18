package com.nikola.jakshic.dagger.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.dagger.model.Peer;
import com.nikola.jakshic.dagger.repository.PeerRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class PeerViewModel extends ViewModel{

    private MutableLiveData<List<Peer>> list;
    private PeerRepository repository;
    private MutableLiveData<Boolean> loading;
    private boolean initialFetch;

    @Inject
    public PeerViewModel(PeerRepository repository) {
        this.repository = repository;
        list = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        loading.setValue(false);
    }

    public void initialFetch(long id) {
        if (!initialFetch) {
            repository.fetchPeers(list, loading, id);
            initialFetch = true;
        }
    }

    public void sort(Comparator<Peer> comparator) {
        if (list.getValue() == null) return;
        List<Peer> sortedList = new ArrayList<>(list.getValue());
        Collections.sort(sortedList, comparator);
        list.setValue(sortedList);
    }

    public void fetchPeers(long id) {
        repository.fetchPeers(list, loading, id);
    }

    public MutableLiveData<List<Peer>> getPeers() {
        return list;
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }
}