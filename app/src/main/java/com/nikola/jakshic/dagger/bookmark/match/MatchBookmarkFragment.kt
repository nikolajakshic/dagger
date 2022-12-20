package com.nikola.jakshic.dagger.bookmark.match

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
import com.nikola.jakshic.dagger.databinding.FragmentBookmarkMatchBinding
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG_MATCH_NOTE_DIALOG = "match-note-dialog"

@AndroidEntryPoint
class MatchBookmarkFragment : Fragment(R.layout.fragment_bookmark_match) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBookmarkMatchBinding.bind(view)
        val viewModel = ViewModelProvider(this)[MatchBookmarkViewModel::class.java]

        val adapter = MatchBookmarkAdapter(
            onClick = {
                findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
            },
            onHold = { matchId, note ->
                if (childFragmentManager.findFragmentByTag(TAG_MATCH_NOTE_DIALOG) == null) {
                    MatchNoteDialog.newInstance(MatchNoteDialogArgs(matchId, note))
                        .showNow(childFragmentManager, TAG_MATCH_NOTE_DIALOG)
                }
            }
        )
        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = adapter

        BookmarkFragment.setOnReselectListener(
            parentFragmentManager,
            viewLifecycleOwner,
            BookmarkFragment.Key.MATCHES
        ) {
            binding.recView.smoothScrollToPosition(0)
        }
        MatchNoteDialog.setOnNoteSavedListener(
            childFragmentManager,
            viewLifecycleOwner
        ) { matchId, note ->
            viewModel.updateNote(matchId, note)
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
}
