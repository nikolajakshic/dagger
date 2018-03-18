package com.nikola.jakshic.dagger.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.dagger.data.local.PlayerDao;
import com.nikola.jakshic.dagger.model.Player;

import java.util.List;

import javax.inject.Inject;

public class BookmarkViewModel extends ViewModel {

    private LiveData<List<Player>> list;

    @Inject
    public BookmarkViewModel(PlayerDao playerDao) {
        list = playerDao.getPlayers();
    }

    public LiveData<List<Player>> getPlayers() {
        return list;
    }
}