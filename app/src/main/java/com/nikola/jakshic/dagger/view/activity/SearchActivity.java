package com.nikola.jakshic.dagger.view.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.DaggerApp;
import com.nikola.jakshic.dagger.model.SearchHistory;
import com.nikola.jakshic.dagger.util.NetworkUtil;
import com.nikola.jakshic.dagger.view.adapter.PlayerAdapter;
import com.nikola.jakshic.dagger.view.adapter.SearchHistoryAdapter;
import com.nikola.jakshic.dagger.viewModel.SearchViewModel;

import javax.inject.Inject;

public class SearchActivity extends AppCompatActivity implements SearchHistoryAdapter.OnQueryClickListener {

    private static final String STATE_QUERY = "query-state";
    private static final String STATE_FOCUS = "searchview-focus";
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private SearchViewModel viewModel;
    private SearchView mSearchView;
    private String mQuery;
    private boolean mFocus = true;
    private RecyclerView recViewSearchHistory;
    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((DaggerApp) getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_search);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);

        if (savedInstanceState != null) {
            mQuery = savedInstanceState.getString(STATE_QUERY);
            mFocus = savedInstanceState.getBoolean(STATE_FOCUS);
        } else {
            // Show search history when activity starts
            viewModel.getAllQueries();
        }

        ProgressBar mProgressBar = findViewById(R.id.progress_search);

        PlayerAdapter mAdapter = new PlayerAdapter(this);
        SearchHistoryAdapter mAdapterHistory = new SearchHistoryAdapter(this, this);

        viewModel.getPlayers().observe(this, mAdapter::addData);

        rootView = findViewById(R.id.view_search_root);

        RecyclerView recViewPlayers = findViewById(R.id.recview_player);
        recViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        recViewPlayers.setAdapter(mAdapter);
        recViewPlayers.setHasFixedSize(true);

        recViewSearchHistory = findViewById(R.id.recview_search_history);
        recViewSearchHistory.setLayoutManager(new LinearLayoutManager(this));
        recViewSearchHistory.setAdapter(mAdapterHistory);
        recViewSearchHistory.setHasFixedSize(true);

        recViewPlayers.setOnTouchListener((v, event) -> {
            mSearchView.clearFocus();
            return false;
        });

        viewModel.isLoading().observe(this, aBoolean -> {
            mProgressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
        });

        viewModel.getSearchHistory().observe(this, searchHistories -> {
            recViewSearchHistory.setVisibility(mFocus ? View.VISIBLE : View.INVISIBLE);
            mAdapterHistory.addData(searchHistories);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_search);
        searchItem.expandActionView();
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setQuery(mQuery, false);
        mSearchView.setQueryHint("Search Players");

        if (mFocus)
            mSearchView.requestFocus();
        else
            mSearchView.clearFocus();

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return false;
            }
        });

        mSearchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            mFocus = hasFocus;
            // Show search history when search view has focus
            TransitionManager.beginDelayedTransition(rootView);
            recViewSearchHistory.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.clearList();
                if (NetworkUtil.isActive(SearchActivity.this)) {
                    viewModel.fetchPlayers(query);
                } else {
                    Toast.makeText(SearchActivity.this, "Check network connection!", Toast.LENGTH_SHORT).show();
                }
                viewModel.saveQuery(new SearchHistory(query));
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.getQueries(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_QUERY, String.valueOf(mSearchView.getQuery()));
        outState.putBoolean(STATE_FOCUS, mSearchView.hasFocus());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(String query) {
        mSearchView.setQuery(query, true);
    }
}