package com.nikola.jakshic.dagger.search

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemSearchHistoryBinding

class HistoryAdapter(
    private val listener: (String) -> Unit
) : ListAdapter<SearchHistoryUI, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_search_history))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemSearchHistoryBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(getItem(position).query)
            }
        }

        fun bind(item: SearchHistoryUI) {
            binding.tvQuery.text = item.query
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchHistoryUI>() {
            override fun areItemsTheSame(
                oldItem: SearchHistoryUI,
                newItem: SearchHistoryUI
            ): Boolean {
                return oldItem.query == newItem.query
            }

            override fun areContentsTheSame(
                oldItem: SearchHistoryUI,
                newItem: SearchHistoryUI
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
