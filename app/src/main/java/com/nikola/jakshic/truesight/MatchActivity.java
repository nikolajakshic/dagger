package com.nikola.jakshic.truesight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nikola.jakshic.truesight.data.remote.OpenDotaService;

import javax.inject.Inject;

public class MatchActivity extends AppCompatActivity {

    @Inject
    OpenDotaService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TrueSightApp)getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
    }
}
