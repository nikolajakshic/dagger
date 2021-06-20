package com.nikola.jakshic.dagger.bookmark.player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.databinding.FragmentBookmarkPlayerBinding
import com.nikola.jakshic.dagger.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerBookmarkFragment : Fragment(R.layout.fragment_bookmark_player),
    HomeFragment.OnNavigationItemReselectedListener {
    private val viewModel by viewModels<PlayerBookmarkViewModel>()

    private var _binding: FragmentBookmarkPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookmarkPlayerBinding.bind(view)

        val adapter = PlayerBookmarkAdapter {
            findNavController().navigate(ProfileFragmentDirections.profileAction(accountId = it.id))
        }

        binding.recView.layoutManager = LinearLayoutManager(activity)
        binding.recView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recView.adapter = adapter
        binding.recView.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.list.collectLatest {
                    adapter.setData(it)
                    if (it.isNotEmpty()) {
                        binding.tvEmptyPlayerBookmark.visibility = View.INVISIBLE
                        binding.recView.visibility = View.VISIBLE
                    } else {
                        binding.tvEmptyPlayerBookmark.visibility = View.VISIBLE
                        binding.recView.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemReselected() {
        binding.recView.smoothScrollToPosition(0)
    }
}