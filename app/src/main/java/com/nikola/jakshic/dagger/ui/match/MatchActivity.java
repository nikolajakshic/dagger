package com.nikola.jakshic.dagger.ui.match;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ProgressBar;

import com.nikola.jakshic.dagger.DaggerApp;
import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.DaggerViewModelFactory;

import javax.inject.Inject;

public class MatchActivity extends AppCompatActivity {

    @Inject
    DaggerViewModelFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((DaggerApp) getApplication()).getAppComponent().inject(this);

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

        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        viewPool.setMaxRecycledViews(0, 10);

        // TODO this is the temporary solution for low performing layout
        // R.layout.item_match_detail_collapse
        // R.layout.item_match_detail_expand
        // Create viewHolders in advance to avoid frame skipping on scroll
        // TODO HIGH PRIORITY
        for (int i = 0; i < 10; i++)
            viewPool.putRecycledView(adapter.createViewHolder(recyclerView, 0));

        recyclerView.setRecycledViewPool(viewPool);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        viewModel.initialFetch(id);
        viewModel.getMatchData().observe(this, adapter::addData);
        viewModel.isLoading().observe(this, aBoolean -> {
            progressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
        });
    }
}