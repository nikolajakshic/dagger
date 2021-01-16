package com.nikola.jakshic.dagger.bookmark.match

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_bookmark_match.*

@AndroidEntryPoint
class MatchBookmarkFragment : Fragment(R.layout.fragment_bookmark_match), HomeFragment.OnNavigationItemReselectedListener, MatchNoteDialog.OnNoteSavedListener {
    private val viewModel by viewModels<MatchBookmarkViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        recView.layoutManager = LinearLayoutManager(activity)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(viewLifecycleOwner) {
            adapter.addData(it)
            if (it?.size ?: 0 != 0) {
                tvEmptyMatchBookmark.visibility = View.INVISIBLE
                recView.visibility = View.VISIBLE
            } else {
                tvEmptyMatchBookmark.visibility = View.VISIBLE
                recView.visibility = View.INVISIBLE
            }
        }
    }

    override fun onItemReselected() {
        recView.smoothScrollToPosition(0)
    }

    override fun onNoteSaved(note: String?, matchId: Long) {
        viewModel.updateNote(note, matchId)
    }
}