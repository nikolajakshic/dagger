package com.nikola.jakshic.dagger.ui.bookmark

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.inflate
import com.nikola.jakshic.dagger.vo.Player
import kotlinx.android.synthetic.main.item_player.view.*

class PlayerAdapter(val listener: (Player) -> Unit) : RecyclerView.Adapter<PlayerAdapter.PlayerVH>() {

    private var list: List<Player>? = null
    private val glideOptions = RequestOptions().circleCrop()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerVH {
        return PlayerVH(parent.inflate(R.layout.item_player))
    }

    override fun onBindViewHolder(holder: PlayerVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(newList: List<Player>?) {
        list = newList
        notifyDataSetChanged()
    }

    inner class PlayerVH(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener(list!![adapterPosition]) }
        }

        fun bind(item: Player) {
            with(itemView) {
                tvPlayerName.text = if (TextUtils.isEmpty(item.name)) item.personaName else item.name
                tvPlayerId.text = item.id.toString()
                Glide.with(this)
                        .load(item.avatarUrl)
                        .apply(glideOptions)
                        .transition(withCrossFade())
                        .into(imgAvatar)
            }
        }
    }
}