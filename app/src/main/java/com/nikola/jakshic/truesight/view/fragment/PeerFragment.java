package com.nikola.jakshic.truesight.view.fragment;


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

import com.nikola.jakshic.truesight.PeerViewModel;
import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.TrueSightApp;
import com.nikola.jakshic.truesight.comparator.PeerComparator;
import com.nikola.jakshic.truesight.util.NetworkUtil;
import com.nikola.jakshic.truesight.view.PeerSortDialog;
import com.nikola.jakshic.truesight.view.adapter.PeerAdapter;
import com.nikola.jakshic.truesight.viewModel.TrueSightViewModelFactory;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeerFragment extends Fragment implements PeerSortDialog.OnSortListener{

    private static final String LOG_TAG = PeerFragment.class.getSimpleName();

    @Inject
    TrueSightViewModelFactory factory;
    private RecyclerView recyclerView;
    private PeerViewModel viewModel;

    public PeerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        ((TrueSightApp) getActivity().getApplication()).getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_peer, container, false);

        long accountId = getActivity().getIntent().getLongExtra("player-account-id", -1);

        viewModel = ViewModelProviders.of(this, factory).get(PeerViewModel.class);
        SwipeRefreshLayout mRefresh = root.findViewById(R.id.swiperefresh_peer);

        PeerAdapter mAdapter = new PeerAdapter(getContext());

        recyclerView = root.findViewById(R.id.recview_peer);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        View btnSort = root.findViewById(R.id.btn_peer_sort);

        btnSort.setOnClickListener(v -> {
            PeerSortDialog dialog = PeerSortDialog.newInstance();
            dialog.setTargetFragment(this, 200);
            dialog.show(getFragmentManager(), null);
        });

        viewModel.initialFetch(accountId);
        viewModel.getPeers().observe(this, mAdapter::addData);
        viewModel.isLoading().observe(this, mRefresh::setRefreshing);

        mRefresh.setOnRefreshListener(() -> {
            if (NetworkUtil.isActive(getActivity())) {
                viewModel.fetchPeers(accountId);
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
                viewModel.sort(new PeerComparator.ByGames());
                break;
            case 1:
                viewModel.sort(new PeerComparator.ByWinRate());
                break;
        }
        recyclerView.scrollToPosition(0);
    }
}
