package com.nikola.jakshic.truesight.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nikola.jakshic.truesight.data.local.PlayerDao;
import com.nikola.jakshic.truesight.model.Player;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private LiveData<List<Player>> list;

    @Inject
    public HomeViewModel(PlayerDao playerDao) {
        list = playerDao.getPlayers();
    }

    public LiveData<List<Player>> getPlayers() {
        return list;
    }
}