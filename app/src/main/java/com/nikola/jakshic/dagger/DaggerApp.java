package com.nikola.jakshic.dagger;

import android.app.Application;

import com.nikola.jakshic.dagger.di.AppComponent;
import com.nikola.jakshic.dagger.di.NetworkModule;
import com.nikola.jakshic.dagger.di.DaggerAppComponent;

public class DaggerApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .networkModule(new NetworkModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}