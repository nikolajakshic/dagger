package com.nikola.jakshic.truesight.view.activity;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.data.remote.OpenDotaClient;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.util.NetworkUtil;
import com.nikola.jakshic.truesight.view.adapter.PlayerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Player>> {

    private static final String STATE_PROGRESS = "progressbar-state";
    private static final String STATE_QUERY = "query-state";
    private static final String STATE_FOCUS = "searchview-focus";
    private static final String BUNDLE_PLAYER_NAME = "player-name";
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private PlayerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private SearchView mSearchView;
    private String mQuery;
    private boolean mFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_search);

        mProgressBar = findViewById(R.id.progress_search);

        mAdapter = new PlayerAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recview_player);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        getLoaderManager().initLoader(0, null, this);
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

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_PLAYER_NAME, query);
                mAdapter.addData(new ArrayList<>());
                if (NetworkUtil.isActive(SearchActivity.this))
                    getLoaderManager().restartLoader(0, bundle, SearchActivity.this);
                else
                    Toast.makeText(SearchActivity.this, "Check network connection!", Toast.LENGTH_SHORT).show();
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_QUERY, String.valueOf(mSearchView.getQuery()));
        outState.putInt(STATE_PROGRESS, mProgressBar.getVisibility());
        outState.putBoolean(STATE_FOCUS, mSearchView.hasFocus());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mQuery = savedInstanceState.getString(STATE_QUERY);
        mFocus = savedInstanceState.getBoolean(STATE_FOCUS);
        mProgressBar.setVisibility(savedInstanceState.getInt(STATE_PROGRESS));
    }

    @Override
    public Loader<List<Player>> onCreateLoader(int id, Bundle args) {
        if (args != null)
            mProgressBar.setVisibility(View.VISIBLE);
        return new PlayerLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Player>> loader, List<Player> data) {
        mProgressBar.setVisibility(View.GONE);
        if (data != null) {
            mAdapter.addData(data);
        } else
            Toast.makeText(this, "Network problem...\nTry again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<List<Player>> loader) {

    }

    private static class PlayerLoader extends AsyncTaskLoader<List<Player>> {

        private List<Player> mData;
        private Bundle bundle;

        public PlayerLoader(Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            if (mData != null)
                deliverResult(mData);
            else if (bundle != null)
                forceLoad();
        }

        @Override
        public List<Player> loadInBackground() {
            List<Player> list = new ArrayList<>();
            String query = bundle.getString(BUNDLE_PLAYER_NAME);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient okClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(OpenDotaClient.BASE_URL)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OpenDotaClient client = retrofit.create(OpenDotaClient.class);

            try {
                list = client.searchPlayers(query).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        public void deliverResult(List<Player> data) {
            mData = data;
            super.deliverResult(data);
        }
    }
}