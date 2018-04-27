package com.nikola.jakshic.dagger.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.*
import com.nikola.jakshic.dagger.diffcallback.PeerDiffCallback
import com.nikola.jakshic.dagger.util.NetworkUtil
import com.nikola.jakshic.dagger.view.PeerSortDialog
import com.nikola.jakshic.dagger.view.adapter.PeerAdapter
import com.nikola.jakshic.dagger.viewModel.DaggerViewModelFactory
import com.nikola.jakshic.dagger.viewModel.PeerViewModel
import kotlinx.android.synthetic.main.fragment_peer.*
import javax.inject.Inject

class PeerFragment : Fragment(), PeerSortDialog.OnSortListener{

    @Inject lateinit var factory: DaggerViewModelFactory
    private var id : Long = -1
    private lateinit var viewModel : PeerViewModel
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

        id = activity?.intent?.getLongExtra("account_id",-1)?: -1

        viewModel = ViewModelProviders.of(this, factory)[PeerViewModel::class.java]

        viewModel.initialFetch(id)

        adapter = PeerAdapter(context, PeerDiffCallback())

        recView.layoutManager = LinearLayoutManager(context)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.peers.observe(this, Observer(adapter::submitList))
        viewModel.status.observe(this, Observer {
            when(it){
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
            else{
                toast("Check network connection!")
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onSort(sortOption: Int) {
        // Remove previous observers b/c we are attaching new LiveData<PagedList>
        viewModel.peers.removeObservers(this)

        when(sortOption){
            0 -> viewModel.sortByGames(id)
            else -> viewModel.sortByWinrate(id)
        }
        // Set to null first, to delete all the items otherwise the list wont be scrolled to the first item
        adapter.submitList(null)
        // Attach the observer to the new LiveData<PagedList>
        viewModel.peers.observe(this, Observer(adapter::submitList))
    }
}