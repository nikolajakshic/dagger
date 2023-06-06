package com.nikola.jakshic.dagger.stream

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemStreamBinding

class StreamAdapter(
    private val listener: (userName: String) -> Unit,
) : ListAdapter<StreamUI, StreamAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_stream))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemStreamBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(getItem(position).userName)
            }
        }

        fun bind(item: StreamUI) {
            binding.tvName.text = item.userName
            binding.tvTitle.text = item.title
            binding.tvViewerCount.text = "${item.viewerCount}"
            // Must be the same aspect ratio as imgThumbnail's layout_width/layout_height.
            val thumbnailUrl = item.thumbnailUrl
                .replace("{width}", "260")
                .replace("{height}", "160")
            binding.imgThumbnail.load(thumbnailUrl)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StreamUI>() {
            override fun areItemsTheSame(oldItem: StreamUI, newItem: StreamUI): Boolean {
                return oldItem.userName == newItem.userName
            }

            override fun areContentsTheSame(oldItem: StreamUI, newItem: StreamUI): Boolean {
                return oldItem == newItem
            }
        }
    }
}
