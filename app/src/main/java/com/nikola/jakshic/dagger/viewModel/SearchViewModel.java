package com.nikola.jakshic.dagger.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.dagger.AppExecutors;
import com.nikola.jakshic.dagger.data.local.SearchHistoryDao;
import com.nikola.jakshic.dagger.model.Player;
import com.nikola.jakshic.dagger.model.SearchHistory;
import com.nikola.jakshic.dagger.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<Player>> playerList;
    private MutableLiveData<List<SearchHistory>> historyList;
    private PlayerRepository repository;
    private SearchHistoryDao searchHistoryDao;
    private MutableLiveData<Boolean> loading;
    private AppExecutors executor;

    @Inject
    public SearchViewModel(AppExecutors executor, PlayerRepository repository, SearchHistoryDao searchHistoryDao) {
        this.repository = repository;
        this.executor = executor;
        this.searchHistoryDao = searchHistoryDao;
        playerList = new MutableLiveData<>();
        historyList = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        loading.setValue(false);
    }

    public void getAllQueries() {
        executor.diskIO().execute(() -> {
            List<SearchHistory> allQueries = searchHistoryDao.getAllQueries();
            historyList.postValue(allQueries);
        });

    }

    public MutableLiveData<List<SearchHistory>> getSearchHistory() {
        return historyList;
    }

    public void getQueries(String query) {
        executor.diskIO().execute(() -> {
            List<SearchHistory> list = searchHistoryDao.getQuery(query);
            historyList.postValue(list);
        });

    }

    public void saveQuery(SearchHistory searchHistory) {
        executor.diskIO().execute(() -> {
            searchHistoryDao.insertQuery(searchHistory);
        });
    }

    public MutableLiveData<List<Player>> getPlayers() {
        return playerList;
    }

    public void fetchPlayers(String name) {
        repository.fetchPlayers(playerList, loading, name);
    }

    public MutableLiveData<Boolean> isLoading() {
        return loading;
    }

    public void clearList() {
        playerList.setValue(new ArrayList<>());
    }
}