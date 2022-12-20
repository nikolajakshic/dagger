package com.nikola.jakshic.dagger.bookmark.player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.bookmark.BookmarkFragment
import com.nikola.jakshic.dagger.databinding.FragmentBookmarkPlayerBinding
import com.nikola.jakshic.dagger.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerBookmarkFragment : Fragment(R.layout.fragment_bookmark_player) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBookmarkPlayerBinding.bind(view)
        val viewModel = ViewModelProvider(this)[PlayerBookmarkViewModel::class.java]

        val adapter = PlayerBookmarkAdapter {
            findNavController().navigate(ProfileFragmentDirections.profileAction(accountId = it.id))
        }

        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = adapter

        BookmarkFragment.setOnReselectListener(
            parentFragmentManager,
            viewLifecycleOwner,
            BookmarkFragment.Key.PLAYERS
        ) {
            binding.recView.smoothScrollToPosition(0)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.list.collectLatest {
                    adapter.submitList(it)
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
}
