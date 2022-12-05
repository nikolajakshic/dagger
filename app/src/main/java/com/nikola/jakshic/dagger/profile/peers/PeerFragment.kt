package com.nikola.jakshic.dagger.profile.peers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.FragmentPeerBinding
import com.nikola.jakshic.dagger.profile.ProfileFragmentArgs
import com.nikola.jakshic.dagger.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeerFragment : Fragment(R.layout.fragment_peer), PeerSortDialog.OnSortListener {
    private val viewModel by viewModels<PeerViewModel>()
    private var adapter: PeerAdapter? = null
    private var id: Long = -1

    private var _binding: FragmentPeerBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPeerBinding.bind(view)

        id = ProfileFragmentArgs.fromBundle(requireParentFragment().requireArguments()).accountId

        viewModel.initialFetch(id)

        adapter = PeerAdapter {
            findNavController().navigate(ProfileFragmentDirections.profileAction(accountId = it))
        }

        binding.recView.layoutManager = LinearLayoutManager(context)
        binding.recView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recView.adapter = adapter
        binding.recView.setHasFixedSize(true)

        viewModel.list.observe(viewLifecycleOwner, Observer(adapter!!::addData))
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> binding.swipeRefresh.isRefreshing = true
                else -> binding.swipeRefresh.isRefreshing = false
            }
        }

        val sortDialog = PeerSortDialog()
        sortDialog.setTargetFragment(this, 300)

        binding.btnSort.setOnClickListener {
            if (!sortDialog.isAdded) sortDialog.show(parentFragmentManager, null)
        }

        binding.swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection()) {
                viewModel.fetchPeers(id)
            } else {
                toast(getString(R.string.error_network_connection))
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

    override fun onSort(sort: Int) {
        val adapter = adapter ?: return // make it non-null

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
