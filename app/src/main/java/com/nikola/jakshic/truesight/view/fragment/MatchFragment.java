package com.nikola.jakshic.truesight.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nikola.jakshic.truesight.MatchFragmentViewModel;
import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.TrueSightApp;
import com.nikola.jakshic.truesight.data.local.PlayerDao;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.util.NetworkUtil;
import com.nikola.jakshic.truesight.view.adapter.MatchAdapter;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchFragment extends Fragment {

    private SwipeRefreshLayout mRefresh;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //TODO REORGANIZUJ DA SE NE POZIVA SVE U ONCREATE, VIEWMODEL TREBA DA SE NALAZI U ACTIVITYATTACHED
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_match, container, false);

        MatchFragmentViewModel viewModel = ViewModelProviders.of(this).get(MatchFragmentViewModel.class);

        mRefresh = root.findViewById(R.id.swiperefresh_match);
        MatchAdapter mAdapter = new MatchAdapter(getActivity());
        //TODO PREKO INTENTA POSALJI SAMO ID A NE CEO PLAEYR OBJECT
        Player player = getActivity().getIntent().getParcelableExtra("player-parcelable");
        RecyclerView recyclerView = root.findViewById(R.id.recview_match);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        //TODO METODA SE POZIVA SVAKI PUT KADA SE VRSI ROTIRANJE UREDJAJA, PREBACITI U DRUGI LIFECYCLE
        viewModel.fetchHeroes(player.getId());
        viewModel.getHeroes().observe(this, mAdapter::addData);
        viewModel.isLoading().observe(this, mRefresh::setRefreshing);

        mRefresh.setOnRefreshListener(() -> {
            if (NetworkUtil.isActive(getActivity())) {
                viewModel.fetchHeroes(player.getId());
            } else {
                Toast.makeText(getActivity(), "Check network connection!", Toast.LENGTH_SHORT).show();
                mRefresh.setRefreshing(false);
            }
        });

        return root;
    }
}