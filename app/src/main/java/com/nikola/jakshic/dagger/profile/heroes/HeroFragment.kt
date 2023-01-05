package com.nikola.jakshic.dagger.profile.heroes

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.FragmentHeroBinding
import com.nikola.jakshic.dagger.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG_HERO_SORT_DIALOG = "hero-sort-dialog"

@AndroidEntryPoint
class HeroFragment : Fragment(R.layout.fragment_hero) {
    companion object {
        private const val EXTRA_ACCOUNT_ID = "account-id"

        fun newInstance(accountId: Long): HeroFragment {
            return HeroFragment().apply {
                arguments = bundleOf(EXTRA_ACCOUNT_ID to accountId)
            }
        }

        fun getAccountId(bundle: Bundle): Long {
            if (!bundle.containsKey(EXTRA_ACCOUNT_ID)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_ACCOUNT_ID" is missing.""")
            }
            return bundle.getLong(EXTRA_ACCOUNT_ID)
        }

        fun getAccountId(savedStateHandle: SavedStateHandle): Long {
            if (!savedStateHandle.contains(EXTRA_ACCOUNT_ID)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_ACCOUNT_ID" is missing.""")
            }
            return savedStateHandle[EXTRA_ACCOUNT_ID]!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHeroBinding.bind(view)
        val viewModel = ViewModelProvider(this)[HeroViewModel::class.java]

        val accountId = getAccountId(requireArguments())

        val adapter = HeroAdapter {
            findNavController().navigate(
                ProfileFragmentDirections.matchesByHeroAction(
                    accountId = accountId,
                    heroId = it
                )
            )
        }

        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = adapter

        binding.btnSort.setOnClickListener {
            if (childFragmentManager.findFragmentByTag(TAG_HERO_SORT_DIALOG) == null) {
                HeroSortDialog().showNow(childFragmentManager, TAG_HERO_SORT_DIALOG)
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection()) {
                viewModel.fetchHeroes()
            } else {
                toast(getString(R.string.error_network_connection))
                binding.swipeRefresh.isRefreshing = false
            }
        }
        HeroSortDialog.setOnSortListener(childFragmentManager, viewLifecycleOwner) { newSortBy ->
            if (newSortBy == viewModel.sortBy) {
                return@setOnSortListener
            }
            // Set to null first, to delete all the items otherwise the list wont be scrolled to the first item.
            adapter.submitList(null)
            viewModel.sortBy(newSortBy)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.list.collectLatest(adapter::submitList)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest {
                    binding.swipeRefresh.isRefreshing = it
                }
            }
        }
    }
}
