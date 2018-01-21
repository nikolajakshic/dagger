package com.nikola.jakshic.truesight;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.nikola.jakshic.truesight.model.Player;

public class DetailViewModel extends AndroidViewModel {

    private LiveData<Player> list;
    private boolean isChecked;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void checkPlayer(long id) {
        if (!isChecked)
            list = Singletons.getDb(getApplication()).playerDao().getPlayer(id);
        isChecked = true;
    }

    public LiveData<Player> getPlayer() {
        return list;
    }

    public boolean isFollowed() {
        return list.getValue() != null;
    }
}