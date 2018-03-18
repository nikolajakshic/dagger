package com.nikola.jakshic.dagger.inspector;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nikola.jakshic.dagger.model.Peer;

import java.text.DecimalFormat;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PeerInspector {

    private Context context;
    private Peer peer;

    public PeerInspector(Context context, Peer peer) {
        this.context = context;
        this.peer = peer;
    }

    public String getPeerName() {
        return peer.getPersonaname();
    }

    public String getGamesWith() {
        return String.valueOf(peer.getWithGames());
    }

    public String getWinRate() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(getPercentage()) + "%";
    }

    public float getPercentage() {
        float games = peer.getWithGames();
        if (games == 0) return 0;
        float win = peer.getWithWin();
        return (win / games) * 100.00f;
    }

    public String getAvatarUrl(){
        return peer.getAvatarfull();
    }

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
}