package com.nikola.jakshic.truesight;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.nikola.jakshic.truesight.data.local.PlayerDao;
import com.nikola.jakshic.truesight.model.Player;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private LiveData<Player> list;
    private PlayerDao playerDao;
    private boolean isChecked;

    @Inject
    public DetailViewModel(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public void checkPlayer(long id) {
        if (!isChecked)
            list = playerDao.getPlayer(id);
        isChecked = true;
    }

    public LiveData<Player> getPlayer() {
        return list;
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