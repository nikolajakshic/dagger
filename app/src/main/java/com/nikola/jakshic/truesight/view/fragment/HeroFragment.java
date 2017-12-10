package com.nikola.jakshic.truesight.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.data.remote.OpenDotaClient;
import com.nikola.jakshic.truesight.model.Hero;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.util.NetworkUtil;
import com.nikola.jakshic.truesight.view.adapter.HeroAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeroFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Hero>> {

    private HeroAdapter mAdapter;
    private SwipeRefreshLayout mRefresh;
    private static final String BUNDLE_PLAYER_ID = "player-id";

    public HeroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hero, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recview_hero);
        mAdapter = new HeroAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        mRefresh = root.findViewById(R.id.swiperefresh_hero);
        Player player = getActivity().getIntent().getParcelableExtra("player-parcelable");
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_PLAYER_ID, player.getID());
        mRefresh.setOnRefreshListener(() -> {
            if (NetworkUtil.isActive(getActivity()))
                getLoaderManager().restartLoader(0, bundle, this);
            else {
                Toast.makeText(getActivity(), "Check network connection!", Toast.LENGTH_SHORT).show();
                mRefresh.setRefreshing(false);
            }
        });
        getLoaderManager().initLoader(0, bundle, this);

        return root;
    }

    @Override
    public Loader<List<Hero>> onCreateLoader(int id, Bundle args) {
        mRefresh.setRefreshing(true);
        return new HeroLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<Hero>> loader, List<Hero> data) {
        mRefresh.setRefreshing(false);
        if (data != null && data.size() != 0) {
            mAdapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Hero>> loader) {

    }

    private static class HeroLoader extends AsyncTaskLoader<List<Hero>> {

        private List<Hero> mData;
        private Bundle bundle;

        public HeroLoader(Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            if (mData != null)
                deliverResult(mData);
            else
                forceLoad();
        }

        @Override
        public List<Hero> loadInBackground() {
            List<Hero> list = new ArrayList<>();
            long id = bundle.getLong(BUNDLE_PLAYER_ID);
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
                list = client.getHeroes(id).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        public void deliverResult(List<Hero> data) {
            mData = data;
            super.deliverResult(data);
        }
    }
}
