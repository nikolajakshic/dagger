package com.nikola.jakshic.truesight;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ProgressBar;

import com.nikola.jakshic.truesight.viewModel.TrueSightViewModelFactory;

import javax.inject.Inject;

public class MatchActivity extends AppCompatActivity {

    @Inject
    TrueSightViewModelFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TrueSightApp) getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        long id = getIntent().getLongExtra("match-id", -1);

        setTitle("Match " + id);

        MatchDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(MatchDetailViewModel.class);

        ProgressBar progressBar = findViewById(R.id.progress_match_detail);
        MatchDetailAdapter adapter = new MatchDetailAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.recview_match_detail);

        // Removes blink when item is expanding/collapsing
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        viewModel.initialFetch(id);
        viewModel.getMatchData().observe(this, adapter::addData);
        viewModel.isLoading().observe(this, aBoolean -> {
            progressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
        });
    }
}