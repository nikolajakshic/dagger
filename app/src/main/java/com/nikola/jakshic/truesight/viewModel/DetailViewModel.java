package com.nikola.jakshic.truesight.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.nikola.jakshic.truesight.data.local.PlayerDao;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.repository.PlayerRepository;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private LiveData<Player> list;
    private MutableLiveData<Player> player;
    private PlayerDao playerDao;
    private boolean isChecked;
    private PlayerRepository repository;

    @Inject
    public DetailViewModel(PlayerDao playerDao, PlayerRepository repository) {
        this.repository = repository;
        this.playerDao = playerDao;
        player = new MutableLiveData<>();
    }

    public void checkPlayer(long id) {
        if (!isChecked)
            list = playerDao.getPlayer(id);
        isChecked = true;
    }

    public LiveData<Player> getPlayer() {
        return list;
    }

    public MutableLiveData<Player> getPlayerWinLoss() {
        return player;
    }

    public void fetchPlayerWinLoss(long id) {
        repository.fetchPlayerWinLoss(player, id);
    }

    public void insertPlayer(Player player) {
        playerDao.insertPlayer(player);
    }

    public boolean isFollowed() {
        return list.getValue() != null;
    }

    public class OnClickListener implements AlertDialog.OnClickListener {

        private long id;

        public OnClickListener(long id) {
            this.id = id;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            playerDao.deletePlayer(id);
        }
    }
}