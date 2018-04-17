package com.nikola.jakshic.dagger;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.nikola.jakshic.dagger.di.AppComponent;
import com.nikola.jakshic.dagger.di.NetworkModule;
import com.nikola.jakshic.dagger.di.DaggerAppComponent;

import io.fabric.sdk.android.Fabric;

public class DaggerApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .networkModule(new NetworkModule(this))
                .build();
        Fabric.with(this, new Crashlytics());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}