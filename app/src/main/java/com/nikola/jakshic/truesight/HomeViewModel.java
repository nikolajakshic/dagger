package com.nikola.jakshic.truesight;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.nikola.jakshic.truesight.model.Player;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<Player>> list;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        list = Singletons.getDb(application).playerDao().getPlayers();
    }

    public LiveData<List<Player>> getPlayers() {
        return list;
    }
}