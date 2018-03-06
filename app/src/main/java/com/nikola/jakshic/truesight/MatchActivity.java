package com.nikola.jakshic.truesight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.nikola.jakshic.truesight.data.remote.OpenDotaService;
import com.nikola.jakshic.truesight.model.match.Match;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchActivity extends AppCompatActivity {

    @Inject
    OpenDotaService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TrueSightApp)getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        long id = getIntent().getLongExtra("match-id", -1);

        ProgressBar progressBar = findViewById(R.id.progress_match_detail);
        MatchDetailAdapter adapter = new MatchDetailAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.recview_match_detail);

        // Removes blink when item is expanding/collapsing
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        progressBar.setVisibility(View.VISIBLE);
        service.getMatch(id).enqueue(new Callback<Match>() {
            @Override
            public void onResponse(Call<Match> call, Response<Match> response) {
                adapter.addData(response.body());
                Log.d("JSONDEBUG: ", "" + response.body().getPlayers().size());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Match> call, Throwable t) {
                Log.d("JSONDEBUG: ", "" + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
