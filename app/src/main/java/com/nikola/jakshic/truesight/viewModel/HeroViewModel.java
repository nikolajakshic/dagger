package com.nikola.jakshic.truesight.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.truesight.model.Hero;
import com.nikola.jakshic.truesight.repository.HeroRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class HeroViewModel extends ViewModel {

    private static final String LOG_TAG = HeroViewModel.class.getSimpleName();

    private MutableLiveData<List<Hero>> list;
    private HeroRepository repository;
    private MutableLiveData<Boolean> loading;
    private boolean initialFetch;

    @Inject
    public HeroViewModel(HeroRepository repository) {
        this.repository = repository;
        list = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        loading.setValue(false);
    }

    public void initialFetch(long id) {
        if (!initialFetch) {
            repository.fetchHeroes(list, loading, id);
            initialFetch = true;
        }
    }

    public void fetchHeroes(long id) {
        repository.fetchHeroes(list, loading, id);
    }

    public void sort(Comparator<Hero> comparator) {
        if (list.getValue() == null) return;
        List<Hero> sortedList = new ArrayList<>(list.getValue());
        Collections.sort(sortedList, comparator);
        list.setValue(sortedList);
    }

    public MutableLiveData<List<Hero>> getHeroes() {
        return list;
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }
}