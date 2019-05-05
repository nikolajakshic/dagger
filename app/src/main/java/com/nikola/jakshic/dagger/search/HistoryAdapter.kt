package com.nikola.jakshic.dagger.search

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import kotlinx.android.synthetic.main.item_search_history.view.*

class HistoryAdapter(val listener: (String) -> Unit) : RecyclerView.Adapter<HistoryAdapter.HistoryVH>() {

    private var list: List<SearchHistory>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryVH {
        return HistoryVH(parent.inflate(R.layout.item_search_history))
    }

    override fun onBindViewHolder(holder: HistoryVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(newList: List<SearchHistory>?) {
        list = newList
        notifyDataSetChanged()
    }

    inner class HistoryVH(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener(list!![adapterPosition].query) }
        }

        fun bind(item: SearchHistory) {
            with(itemView) {
                tvQuery.text = item.query
            }
        }
    }
}