package com.nikola.jakshic.dagger.bookmark.player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_bookmark_player.*

@AndroidEntryPoint
class PlayerBookmarkFragment : Fragment(R.layout.fragment_bookmark_player), HomeFragment.OnNavigationItemReselectedListener {
    private val viewModel by viewModels<PlayerBookmarkViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlayerBookmarkAdapter {
            findNavController().navigate(ProfileFragmentDirections.profileAction(accountId = it.id))
        }

        recView.layoutManager = LinearLayoutManager(activity)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(viewLifecycleOwner) {
            adapter.addData(it)
            if (it?.size ?: 0 != 0) {
                tvEmptyPlayerBookmark.visibility = View.INVISIBLE
                recView.visibility = View.VISIBLE
            } else {
                tvEmptyPlayerBookmark.visibility = View.VISIBLE
                recView.visibility = View.INVISIBLE
            }
        }
    }

    override fun onItemReselected() {
        recView.smoothScrollToPosition(0)
    }
}