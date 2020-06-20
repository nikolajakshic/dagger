package com.nikola.jakshic.dagger.stream

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import kotlinx.android.synthetic.main.item_stream.view.*

class StreamAdapter(
    private val listener: (userName: String) -> Unit
) : RecyclerView.Adapter<StreamAdapter.StreamViewHolder>() {

    private var list: List<StreamUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        return StreamViewHolder(parent.inflate(R.layout.item_stream))
    }

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        holder.bind(list!![position])
    }

    fun addData(list: List<StreamUI>?) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class StreamViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener(list!![adapterPosition].userName) }
        }

        fun bind(item: StreamUI) {
            with(itemView) {
                tvName.text = item.userName
                tvTitle.text = item.title
                tvViewerCount.text = "${item.viewerCount}"
                // Must be the same aspect ratio as imgThumbnail's layout_width/layout_height.
                val thumbnailUrl = item.thumbnailUrl
                    .replace("{width}", "260")
                    .replace("{height}", "160")
                imgThumbnail.load(thumbnailUrl)
            }
        }
    }
}