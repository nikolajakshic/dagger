package com.nikola.jakshic.dagger.ui.profile.peer

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
import com.nikola.jakshic.dagger.*
import com.nikola.jakshic.dagger.util.NetworkUtil
import com.nikola.jakshic.dagger.ui.profile.ProfileActivity
import com.nikola.jakshic.dagger.viewModel.DaggerViewModelFactory
import kotlinx.android.synthetic.main.fragment_peer.*
import javax.inject.Inject

class PeerFragment : Fragment(), PeerSortDialog.OnSortListener {

    @Inject lateinit var factory: DaggerViewModelFactory
    private var id: Long = -1
    private lateinit var viewModel: PeerViewModel
    private lateinit var adapter: PeerAdapter

    override fun onAttach(context: Context?) {
        (activity?.application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_peer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = activity?.intent?.getLongExtra("account_id", -1) ?: -1

        viewModel = ViewModelProviders.of(this, factory)[PeerViewModel::class.java]

        viewModel.initialFetch(id)

        adapter = PeerAdapter {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("account_id", it)
            startActivity(intent)
        }

        recView.layoutManager = LinearLayoutManager(context)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(this, Observer(adapter::addData))
        viewModel.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> swipeRefresh.isRefreshing = true
                else -> swipeRefresh.isRefreshing = false
            }
        })

        val sortDialog = PeerSortDialog.newInstance()
        sortDialog.setTargetFragment(this, 300)
        btnSort.setOnClickListener {
            sortDialog.show(fragmentManager, null)
        }

        swipeRefresh.setOnRefreshListener {
            if (NetworkUtil.isActive(context))
                viewModel.fetchPeers(id)
            else {
                toast("Check network connection!")
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onSort(sort: Int) {
        // Remove previous observers b/c we are attaching new LiveData<PagedList>
        viewModel.list.removeObservers(this)

        when (sort) {
            0 -> viewModel.sortByGames(id)
            else -> viewModel.sortByWinRate(id)
        }
        // Set to null first, to delete all the items otherwise the list wont be scrolled to the first item
        adapter.addData(null)
        // Attach the observer to the new LiveData<PagedList>
        viewModel.list.observe(this, Observer(adapter::addData))
    }
}