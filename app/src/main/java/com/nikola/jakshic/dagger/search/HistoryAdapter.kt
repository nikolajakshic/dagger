package com.nikola.jakshic.dagger.search

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemSearchHistoryBinding

class HistoryAdapter(
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryVH>() {
    private var list: List<SearchHistoryUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryVH {
        return HistoryVH(parent.inflate(R.layout.item_search_history))
    }

    override fun onBindViewHolder(holder: HistoryVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(newList: List<SearchHistoryUI>?) {
        list = newList
        notifyDataSetChanged()
    }

    inner class HistoryVH(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemSearchHistoryBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(list!![position].query)
            }
        }

        fun bind(item: SearchHistoryUI) {
            binding.tvQuery.text = item.query
        }
    }
}