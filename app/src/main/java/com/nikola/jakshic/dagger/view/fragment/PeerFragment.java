/*package com.nikola.jakshic.dagger.view.fragment;

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
import com.nikola.jakshic.dagger.diffcallback.PeerDiffCallback;
import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.util.NetworkUtil;
import com.nikola.jakshic.dagger.view.PeerSortDialog;
import com.nikola.jakshic.dagger.view.adapter.PeerAdapter;
import com.nikola.jakshic.dagger.viewModel.PeerViewModel;
import com.nikola.jakshic.dagger.viewModel.DaggerViewModelFactory;

import javax.inject.Inject;

public class PeerFragment extends Fragment implements PeerSortDialog.OnSortListener {

    @Inject
    DaggerViewModelFactory factory;
    private PeerAdapter mAdapter;
    private PeerViewModel viewModel;
    private long accountId;

    public PeerFragment() {
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_peer, container, false);

        accountId = getActivity().getIntent().getLongExtra("account_id", -1);

        viewModel = ViewModelProviders.of(this, factory).get(PeerViewModel.class);
        SwipeRefreshLayout mRefresh = root.findViewById(R.id.swiperefresh_peer);

        mAdapter = new PeerAdapter(getContext(), new PeerDiffCallback());

        RecyclerView recyclerView = root.findViewById(R.id.recview_peer);
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
        viewModel.getPeers().observe(this, mAdapter::submitList);
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

        // Remove previous observers b/c we are attaching new LiveData<PagedList>
        viewModel.getPeers().removeObservers(this);

        switch (sortOption) {
            case 0:
                viewModel.sortByGames(accountId);
                break;
            case 1:
                viewModel.sortByWinrate(accountId);
                break;
        }

        // Set to null first, to delete all the items otherwise the list wont be scrolled to the first item
        mAdapter.submitList(null);

        // Attach the observer to the new LiveData<PagedList>
        viewModel.getPeers().observe(this, mAdapter::submitList);

    }
}*/