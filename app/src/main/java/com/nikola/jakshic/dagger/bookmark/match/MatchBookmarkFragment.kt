package com.nikola.jakshic.dagger.bookmark.match

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
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.bookmark.BookmarkFragment
import com.nikola.jakshic.dagger.databinding.FragmentBookmarkMatchBinding
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchBookmarkFragment :
    Fragment(R.layout.fragment_bookmark_match),
    MatchNoteDialog.OnNoteSavedListener {
    private val viewModel by viewModels<MatchBookmarkViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBookmarkMatchBinding.bind(view)

        val adapter = MatchBookmarkAdapter(
            onClick = {
                findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
            },
            onHold = { note, matchId ->
                val dialog = MatchNoteDialog.newInstance(note, matchId)
                dialog.setTargetFragment(this, 501)
                dialog.show(parentFragmentManager, null)
            }
        )

        binding.recView.layoutManager = LinearLayoutManager(activity)
        binding.recView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recView.adapter = adapter
        binding.recView.setHasFixedSize(true)

        BookmarkFragment.setOnReselectListener(
            parentFragmentManager,
            viewLifecycleOwner,
            BookmarkFragment.Key.MATCHES
        ) {
            binding.recView.smoothScrollToPosition(0)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.list.collectLatest {
                    adapter.submitList(it)
                    if (it.isNotEmpty()) {
                        binding.tvEmptyMatchBookmark.visibility = View.INVISIBLE
                        binding.recView.visibility = View.VISIBLE
                    } else {
                        binding.tvEmptyMatchBookmark.visibility = View.VISIBLE
                        binding.recView.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    override fun onNoteSaved(note: String?, matchId: Long) {
        viewModel.updateNote(note, matchId)
    }
}
