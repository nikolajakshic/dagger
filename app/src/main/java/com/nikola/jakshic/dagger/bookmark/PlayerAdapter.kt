package com.nikola.jakshic.dagger.bookmark

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.profile.Player
import kotlinx.android.synthetic.main.item_player.view.*

class PlayerAdapter(val listener: (Player) -> Unit) : RecyclerView.Adapter<PlayerAdapter.PlayerVH>() {

    private var list: List<Player>? = null

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
                imgAvatar.load(item.avatarUrl) {
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}