package com.nikola.jakshic.dagger.bookmark.match

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.HomeActivity
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.matchstats.MatchStatsActivity
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
                val intent = Intent(context, MatchStatsActivity::class.java)
                intent.putExtra("match_id", it)
                startActivity(intent)
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

        viewModel.list.observe(viewLifecycleOwner, Observer(adapter::addData))
    }

    override fun onItemReselected() {
        recView.smoothScrollToPosition(0)
    }

    override fun onNoteSaved(note: String?, matchId: Long) {
        viewModel.updateNote(note, matchId)
    }
}