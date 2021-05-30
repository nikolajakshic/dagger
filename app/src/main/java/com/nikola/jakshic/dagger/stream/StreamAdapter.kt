package com.nikola.jakshic.dagger.stream

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemStreamBinding

class StreamAdapter(
    private val listener: (userName: String) -> Unit
) : RecyclerView.Adapter<StreamAdapter.StreamViewHolder>() {
    private var list: List<StreamUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        return StreamViewHolder(parent.inflate(R.layout.item_stream))
    }

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun setData(list: List<StreamUI>?) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class StreamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemStreamBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(list!![position].userName)
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
}