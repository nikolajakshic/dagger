package com.nikola.jakshic.dagger.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nikola.jakshic.dagger.DaggerApp;
import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.diffcallback.CompetitiveDiffCallback;
import com.nikola.jakshic.dagger.util.NetworkUtil;
import com.nikola.jakshic.dagger.view.adapter.CompetitiveAdapter;
import com.nikola.jakshic.dagger.viewModel.CompetitiveViewModel;
import com.nikola.jakshic.dagger.viewModel.DaggerViewModelFactory;

import javax.inject.Inject;

public class CompetitiveFragment extends Fragment {

    @Inject
    DaggerViewModelFactory factory;

    public CompetitiveFragment() {
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

        View root = inflater.inflate(R.layout.fragment_competitive, container, false);

        CompetitiveViewModel viewModel = ViewModelProviders.of(this, factory).get(CompetitiveViewModel.class);

        SwipeRefreshLayout refresh = root.findViewById(R.id.swiperefresh_competitive);
        RecyclerView recyclerView = root.findViewById(R.id.recview_match_pro);
        CompetitiveAdapter adapter = new CompetitiveAdapter(getContext(), new CompetitiveDiffCallback());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        viewModel.initialFetch();
        viewModel.getCompetitiveMatches().observe(this, adapter::submitList);
        viewModel.getStatus().observe(this, status -> {
            switch (status) {
                case LOADING:
                    refresh.setRefreshing(true);
                    break;
                default:
                    refresh.setRefreshing(false);
                    break;
            }
        });

        refresh.setOnRefreshListener(() -> {
            if (NetworkUtil.isActive(getActivity())) {
                viewModel.refreshData();
            } else {
                refresh.setRefreshing(false);
                Toast.makeText(getActivity(), "Check network connection!", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // This fragment is an item from BottomNavigationView
        // Set the proper title when this fragment is not hidden
        if (!isHidden()) getActivity().setTitle("Competitive");
    }
}