package com.nikola.jakshic.dagger.ui.stream

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.di.GlideApp
import com.nikola.jakshic.dagger.inflate
import com.nikola.jakshic.dagger.vo.Stream
import kotlinx.android.synthetic.main.item_stream.view.*

class StreamAdapter(
        private val listener: (userName: String) -> Unit
) : RecyclerView.Adapter<StreamAdapter.StreamViewHolder>() {

    private var list: List<Stream>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        return StreamViewHolder(parent.inflate(R.layout.item_stream))
    }

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        holder.bind(list!![position])
    }

    fun addData(list: List<Stream>?) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class StreamViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener(list!![adapterPosition].userName) }
        }

        fun bind(item: Stream) {
            with(itemView) {
                tvName.text = item.userName
                tvTitle.text = item.title
                tvViewerCount.text = "${item.viewerCount}"
                // Must be the same aspect ratio as imgThumbnail's layout_width/layout_height.
                val thumbnailUrl = item.thumbnailUrl
                        .replace("{width}", "260")
                        .replace("{height}", "160")
                GlideApp.with(this).load(thumbnailUrl).into(imgThumbnail)
            }
        }
    }
}