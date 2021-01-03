package com.nikola.jakshic.dagger.bookmark.player

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
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.profile.ProfileFragmentDirections
import kotlinx.android.synthetic.main.fragment_bookmark_player.*
import javax.inject.Inject

class PlayerBookmarkFragment : Fragment(), HomeFragment.OnNavigationItemReselectedListener {

    @Inject lateinit var factory: DaggerViewModelFactory

    override fun onAttach(context: Context) {
        (activity?.application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_bookmark_player)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this, factory)[PlayerBookmarkViewModel::class.java]

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