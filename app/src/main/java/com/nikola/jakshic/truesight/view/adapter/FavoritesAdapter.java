package com.nikola.jakshic.truesight.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nikola.jakshic.truesight.data.local.DotaContract.DotaSubscriber;
import com.nikola.jakshic.truesight.databinding.ItemPlayerCursorBinding;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.view.activity.DetailActivity;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.BindingHolder> {

    private Cursor cursor;
    private Context context;

    public FavoritesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPlayerCursorBinding binding = ItemPlayerCursorBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bind(cursor, context);
    }

    @Override
    public int getItemCount() {
        if (cursor != null)
            return cursor.getCount();
        else
            return 0;
    }

    public void addData(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public class BindingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemPlayerCursorBinding binding;
        private String name, url, id;

        public BindingHolder(ItemPlayerCursorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void bind(Cursor cursor, Context context) {
            cursor.moveToPosition(getAdapterPosition());
            name = cursor.getString(cursor.getColumnIndex(DotaSubscriber.COLUMN_PLAYER_NAME));
            id = cursor.getString(cursor.getColumnIndex(DotaSubscriber.COLUMN_ACC_ID));
            url = cursor.getString(cursor.getColumnIndex(DotaSubscriber.COLUMN_AVATAR_URL));
            Log.v("FavoritesAdapter: ", cursor.getCount() + " name: " + name + " id: " + id + " url: " + url);
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imagePlayerAvatar);
            binding.textPlayeId.setText(id);
            binding.textPlayerName.setText(name);
        }

        @Override
        public void onClick(View v) {
            Player player = new Player();
            player.setData(Long.valueOf(id), name, url);

            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("player-parcelable", player);
            context.startActivity(intent);
        }
    }
}
