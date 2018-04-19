package com.nikola.jakshic.dagger.view.fragment;

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

import com.nikola.jakshic.dagger.DaggerApp;
import com.nikola.jakshic.dagger.diffcallback.HeroDiffCallback;
import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.util.NetworkUtil;
import com.nikola.jakshic.dagger.view.HeroSortDialog;
import com.nikola.jakshic.dagger.view.adapter.HeroAdapter;
import com.nikola.jakshic.dagger.viewModel.HeroViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeroFragment extends Fragment implements HeroSortDialog.OnSortListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private RecyclerView recyclerView;
    private HeroViewModel viewModel;
    private HeroAdapter mAdapter;
    private long accountId;

    public HeroFragment() {
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
        View root = inflater.inflate(R.layout.fragment_hero, container, false);

        SwipeRefreshLayout mRefresh = root.findViewById(R.id.swiperefresh_hero);

        accountId = getActivity().getIntent().getLongExtra("player-account-id", -1);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HeroViewModel.class);

        recyclerView = root.findViewById(R.id.recview_hero);

        mAdapter = new HeroAdapter(getActivity(), new HeroDiffCallback());

        View btnSort = root.findViewById(R.id.btn_hero_sort);

        btnSort.setOnClickListener(v -> {
            HeroSortDialog dialog = HeroSortDialog.newInstance();
            dialog.setTargetFragment(this, 300);
            dialog.show(getFragmentManager(), null);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        viewModel.initialFetch(accountId);
        viewModel.getHeroes().observe(this, mAdapter::submitList);
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
    public void onSort(int sortOption) {

        // Remove previous observers b/c we are attaching new LiveData<PagedList>
        viewModel.getHeroes().removeObservers(this);

        switch (sortOption) {
            case 0:
                viewModel.sortByGames(accountId);
                break;
            case 1:
                viewModel.sortByWinrate(accountId);
                break;
            case 2:
                viewModel.sortByWins(accountId);
                break;
            case 3:
                viewModel.sortByLosses(accountId);
                break;
        }

        // Set to null first, to delete all the items otherwise the list wont be scrolled to the first item
        mAdapter.submitList(null);

        // Attach the observer to the new LiveData<PagedList>
        viewModel.getHeroes().observe(this, mAdapter::submitList);
    }
}