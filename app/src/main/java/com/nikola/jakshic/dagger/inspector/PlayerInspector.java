package com.nikola.jakshic.dagger.inspector;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nikola.jakshic.dagger.BR;
import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.model.Player;
import com.nikola.jakshic.dagger.view.activity.PlayerActivity;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Inspector objects are used in XML files for Data Binding
 * If any changes needs to be done with Value Objects (models)
 * e.g. date is in milliseconds and needs to be converted to String
 * that is done in Inspector classes.
 */
public class PlayerInspector extends BaseObservable {

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
        return player.getPersonaName();
    }

    public String getPlayerID() {
        return String.valueOf(player.getId());
    }

    @Bindable
    public String getPlayerGames() {
        return context.getString(R.string.player_games, player.getWins() + player.getLosses());
    }

    @Bindable
    public String getPlayerWins() {
        return context.getString(R.string.player_wins, player.getWins());
    }

    @Bindable
    public String getPlayerLosses() {
        return context.getString(R.string.player_losses, player.getLosses());
    }

    @Bindable
    public String getPlayerWinrate() {
        DecimalFormat df = new DecimalFormat("0.00");
        float games = player.getWins() + player.getLosses();
        if (games == 0) return context.getString(R.string.player_winrate, df.format(games));
        float win = player.getWins();
        float winRate = (win / games) * 100.00f;
        return context.getString(R.string.player_winrate, df.format(winRate));
    }

    public void setPlayerWins(long wins) {
        player.setWins(wins);
        notifyPropertyChanged(BR._all);
    }

    public void setPlayerLosses(long losses) {
        player.setLosses(losses);
        notifyPropertyChanged(BR._all);
    }

    // TODO should not be in this file
    @BindingAdapter("avatarUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        if (url != null) {
            RequestOptions option = new RequestOptions().circleCrop();
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(option)
                    .transition(withCrossFade())
                    .into(imageView);
        }
    }

    // TODO should not be in this file
    @BindingAdapter("circleAvatarUrl")
    public static void setImageUrl(CircleImageView imageView, String url) {
        if (url != null) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .into(imageView);
        }
    }

    // TODO should not be in this file
    public void onClick(View v) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("player-personaname", player.getPersonaName());
        intent.putExtra("player-account-id", player.getId());
        intent.putExtra("player-avatar-full", player.getAvatarUrl());
        context.startActivity(intent);
    }
}