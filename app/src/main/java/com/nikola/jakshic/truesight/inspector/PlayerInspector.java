package com.nikola.jakshic.truesight.inspector;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.view.activity.DetailActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PlayerInspector {

    private Context context;
    private Player player;

    public PlayerInspector(Context context, Player player) {
        this.context = context;
        this.player = player;
    }

    public String getImageUrl() {
        return player.getAvatarUrl();
    }

    public String getPlayerName() {
        return player.getName();
    }

    public String getPlayerID() {
        return String.valueOf(player.getId());
    }

    @BindingAdapter("avatarUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        RequestOptions option = new RequestOptions().circleCrop();
        Glide.with(imageView.getContext())
                .load(url)
                .apply(option)
                .transition(withCrossFade())
                .into(imageView);
    }

    @BindingAdapter("circleAvatarUrl")
    public static void setImageUrl(CircleImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    public void onClick(View v){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("player-parcelable", player);
        context.startActivity(intent);
    }
}