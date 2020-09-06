package com.nikola.jakshic.dagger.bookmark.player

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.HomeActivity
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_bookmark_player.*

@AndroidEntryPoint
class PlayerBookmarkFragment : Fragment(), HomeActivity.OnNavigationItemReselectedListener {
    private val viewModel: PlayerBookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_bookmark_player)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlayerBookmarkAdapter {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("account_id", it.id)
            startActivity(intent)
        }

        recView.layoutManager = LinearLayoutManager(activity)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(viewLifecycleOwner, Observer {
            adapter.addData(it)
            if (it?.size ?: 0 != 0) {
                tvEmptyPlayerBookmark.visibility = View.INVISIBLE
                recView.visibility = View.VISIBLE
            } else {
                tvEmptyPlayerBookmark.visibility = View.VISIBLE
                recView.visibility = View.INVISIBLE
            }
        })
    }

    override fun onItemReselected() {
        recView.smoothScrollToPosition(0)
    }
}