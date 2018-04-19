package com.nikola.jakshic.dagger.view.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nikola.jakshic.dagger.DaggerApp;
import com.nikola.jakshic.dagger.diffcallback.MatchDiffCallback;
import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.util.NetworkUtil;
import com.nikola.jakshic.dagger.view.activity.MatchActivity;
import com.nikola.jakshic.dagger.view.adapter.MatchAdapter;
import com.nikola.jakshic.dagger.viewModel.MatchViewModel;

import javax.inject.Inject;

public class MatchFragment extends Fragment implements MatchAdapter.OnMatchClickListener {

    private SwipeRefreshLayout mRefresh;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        ((DaggerApp) getActivity().getApplication()).getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_match, container, false);

        MatchViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MatchViewModel.class);

        mRefresh = root.findViewById(R.id.swiperefresh_match);
        MatchAdapter mAdapter = new MatchAdapter(getActivity(), this, new MatchDiffCallback());

        long accountId = getActivity().getIntent().getLongExtra("player-account-id", -1);
        RecyclerView recyclerView = root.findViewById(R.id.recview_match);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        viewModel.initialFetch(accountId);
        viewModel.getMatches().observe(this, list -> {

            mAdapter.submitList(list);
        });
        viewModel.getStatus().observe(this, status -> {
            switch (status) {
                case LOADING:
                    mRefresh.setRefreshing(true);
                    break;
                default:
                    mRefresh.setRefreshing(false);
                    break;
            }
        });

        mRefresh.setOnRefreshListener(() -> {
            if (NetworkUtil.isActive(getActivity())) {
                viewModel.fetchHeroes(accountId);
            } else {
                Toast.makeText(getActivity(), "Check network connection!", Toast.LENGTH_SHORT).show();
                mRefresh.setRefreshing(false);
            }
        });

        return root;
    }

    @Override
    public void onClick(long id) {
        Intent intent = new Intent(getContext(), MatchActivity.class);
        intent.putExtra("match-id", id);
        startActivity(intent);
    }
}