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

import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.TrueSightApp;
import com.nikola.jakshic.truesight.comparator.HeroComparator;
import com.nikola.jakshic.truesight.util.NetworkUtil;
import com.nikola.jakshic.truesight.view.HeroSortDialog;
import com.nikola.jakshic.truesight.view.adapter.HeroAdapter;
import com.nikola.jakshic.truesight.viewModel.HeroViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeroFragment extends Fragment implements HeroSortDialog.OnSortListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private RecyclerView recyclerView;
    private HeroViewModel viewModel;

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
        long accountId = getActivity().getIntent().getLongExtra("player-account-id", -1);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HeroViewModel.class);

        recyclerView = root.findViewById(R.id.recview_hero);
        HeroAdapter mAdapter = new HeroAdapter(getActivity());

        View btnSort = root.findViewById(R.id.btn_hero_sort);

        btnSort.setOnClickListener(v -> {
            HeroSortDialog dialog = HeroSortDialog.newInstance();
            dialog.setTargetFragment(this, 300);
            dialog.show(getFragmentManager(), null);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        //TODO METODA SE POZIVA SVAKI PUT KADA SE VRSI ROTIRANJE UREDJAJA, PREBACITI U DRUGI LIFECYCLE
        viewModel.initialFetch(accountId);
        viewModel.getHeroes().observe(this, mAdapter::addData);
        viewModel.isLoading().observe(this, mRefresh::setRefreshing);

        //TODO PREKO INTENTA POSALJI SAMO ID A NE CEO PLAEYR OBJECT

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
    public void onSort(int sortOption) {
        switch (sortOption) {
            case 0:
                viewModel.sort(new HeroComparator.ByGames());
                break;
            case 1:
                viewModel.sort(new HeroComparator.ByWinRate());
                break;
            case 2:
                viewModel.sort(new HeroComparator.ByWins());
                break;
            case 3:
                viewModel.sort(new HeroComparator.ByLosses());
                break;
        }
        recyclerView.scrollToPosition(0);
    }
}