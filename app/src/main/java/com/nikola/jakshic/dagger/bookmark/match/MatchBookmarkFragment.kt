package com.nikola.jakshic.dagger.bookmark.match

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.HomeActivity
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import kotlinx.android.synthetic.main.fragment_bookmark_match.*
import javax.inject.Inject

class MatchBookmarkFragment : Fragment(), HomeActivity.OnNavigationItemReselectedListener, MatchNoteDialog.OnNoteSavedListener {
    @Inject lateinit var factory: DaggerViewModelFactory
    private lateinit var viewModel: MatchBookmarkViewModel

    override fun onAttach(context: Context) {
        (activity?.application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory)[MatchBookmarkViewModel::class.java]

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