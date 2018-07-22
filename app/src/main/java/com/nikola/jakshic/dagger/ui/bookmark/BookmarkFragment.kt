package com.nikola.jakshic.dagger.ui.bookmark

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.inflate
import com.nikola.jakshic.dagger.ui.DaggerViewModelFactory
import com.nikola.jakshic.dagger.ui.HomeActivity
import com.nikola.jakshic.dagger.ui.profile.ProfileActivity
import com.nikola.jakshic.dagger.ui.search.SearchActivity
import com.nikola.jakshic.dagger.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_bookmark.*
import javax.inject.Inject

class BookmarkFragment : Fragment(), HomeActivity.OnNavigationItemReselectedListener {

    @Inject lateinit var factory: DaggerViewModelFactory

    override fun onAttach(context: Context?) {
        (activity?.application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_bookmark)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        toolbar.inflateMenu(R.menu.menu_home)

        val viewModel = ViewModelProviders.of(this, factory)[BookmarkViewModel::class.java]

        val adapter = PlayerAdapter {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("account_id", it.id)
            startActivity(intent)
        }

        recView.layoutManager = LinearLayoutManager(activity)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(this, Observer(adapter::addData))

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_home_search -> {
                    startActivity(Intent(activity, SearchActivity::class.java))
                    true
                }
                R.id.menu_home_settings -> {
                    startActivity(Intent(activity, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onItemReselected() {
        recView.smoothScrollToPosition(0)
    }
}