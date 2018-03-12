package com.nikola.jakshic.truesight.view.fragment;

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

import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.TrueSightApp;
import com.nikola.jakshic.truesight.util.NetworkUtil;
import com.nikola.jakshic.truesight.view.activity.MatchActivity;
import com.nikola.jakshic.truesight.view.adapter.MatchAdapter;
import com.nikola.jakshic.truesight.viewModel.MatchViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchFragment extends Fragment implements MatchAdapter.OnMatchClickListener {

    private SwipeRefreshLayout mRefresh;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        ((TrueSightApp) getActivity().getApplication()).getAppComponent().inject(this);
        super.onAttach(context);
    }

    //TODO REORGANIZUJ DA SE NE POZIVA SVE U ONCREATE, VIEWMODEL TREBA DA SE NALAZI U ACTIVITYATTACHED
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_match, container, false);

        MatchViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MatchViewModel.class);

        mRefresh = root.findViewById(R.id.swiperefresh_match);
        MatchAdapter mAdapter = new MatchAdapter(getActivity(), this);
        //TODO PREKO INTENTA POSALJI SAMO ID A NE CEO PLAEYR OBJECT
        long accountId = getActivity().getIntent().getLongExtra("player-account-id", -1);
        RecyclerView recyclerView = root.findViewById(R.id.recview_match);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        //TODO METODA SE POZIVA SVAKI PUT KADA SE VRSI ROTIRANJE UREDJAJA, PREBACITI U DRUGI LIFECYCLE
        viewModel.initialFetch(accountId);
        viewModel.getMatches().observe(this, mAdapter::addData);
        viewModel.isLoading().observe(this, mRefresh::setRefreshing);

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