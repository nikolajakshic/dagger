package com.nikola.jakshic.dagger.profile.peers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.profile.ProfileFragmentArgs
import com.nikola.jakshic.dagger.profile.ProfileFragmentDirections
import kotlinx.android.synthetic.main.fragment_peer.*
import javax.inject.Inject

class PeerFragment : Fragment(), PeerSortDialog.OnSortListener {

    @Inject lateinit var factory: DaggerViewModelFactory
    private var id: Long = -1
    private lateinit var viewModel: PeerViewModel
    private lateinit var adapter: PeerAdapter

    override fun onAttach(context: Context) {
        (activity?.application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_peer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = ProfileFragmentArgs.fromBundle(requireParentFragment().requireArguments()).accountId

        viewModel = ViewModelProviders.of(this, factory)[PeerViewModel::class.java]

        viewModel.initialFetch(id)

        adapter = PeerAdapter {
            findNavController().navigate(ProfileFragmentDirections.profileAction(accountId = it))
        }

        recView.layoutManager = LinearLayoutManager(context)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(viewLifecycleOwner, Observer(adapter::addData))
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> swipeRefresh.isRefreshing = true
                else -> swipeRefresh.isRefreshing = false
            }
        }

        val sortDialog = PeerSortDialog()
        sortDialog.setTargetFragment(this, 300)

        btnSort.setOnClickListener {
            if (!sortDialog.isAdded) sortDialog.show(parentFragmentManager, null)
        }

        swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection())
                viewModel.fetchPeers(id)
            else {
                toast(getString(R.string.error_network_connection))
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onSort(sort: Int) {
        // Remove previous observers b/c we are attaching new LiveData
        viewModel.list.removeObservers(viewLifecycleOwner)

        when (sort) {
            0 -> viewModel.sortByGames(id)
            else -> viewModel.sortByWinRate(id)
        }
        // Set to null first, to delete all the items otherwise the list wont be scrolled to the first item
        adapter.addData(null)
        // Attach the observer to the new LiveData
        viewModel.list.observe(viewLifecycleOwner, Observer(adapter::addData))
    }
}