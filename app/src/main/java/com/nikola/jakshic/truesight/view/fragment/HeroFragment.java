package com.nikola.jakshic.truesight.view.fragment;

import android.arch.lifecycle.ViewModelProvider;
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

import com.nikola.jakshic.truesight.HeroFragmentViewModel;
import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.TrueSightApp;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.util.NetworkUtil;
import com.nikola.jakshic.truesight.view.adapter.HeroAdapter;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeroFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public HeroFragment() {
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
        View root = inflater.inflate(R.layout.fragment_hero, container, false);

        SwipeRefreshLayout mRefresh = root.findViewById(R.id.swiperefresh_hero);
        Player player = getActivity().getIntent().getParcelableExtra("player-parcelable");
        HeroFragmentViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(HeroFragmentViewModel.class);

        RecyclerView recyclerView = root.findViewById(R.id.recview_hero);
        HeroAdapter mAdapter = new HeroAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //TODO METODA SE POZIVA SVAKI PUT KADA SE VRSI ROTIRANJE UREDJAJA, PREBACITI U DRUGI LIFECYCLE
        viewModel.fetchHeroes(player.getId());
        viewModel.getHeroes().observe(this, mAdapter::addData);
        viewModel.isLoading().observe(this, mRefresh::setRefreshing);

        //TODO PREKO INTENTA POSALJI SAMO ID A NE CEO PLAEYR OBJECT

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