package com.nikola.jakshic.truesight;

import android.app.Application;

import com.nikola.jakshic.truesight.di.AppComponent;
import com.nikola.jakshic.truesight.di.AppModule;
import com.nikola.jakshic.truesight.di.DaggerAppComponent;

public class TrueSightApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
