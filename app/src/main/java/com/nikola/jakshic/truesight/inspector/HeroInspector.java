package com.nikola.jakshic.truesight.inspector;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nikola.jakshic.truesight.model.Hero;
import com.nikola.jakshic.truesight.util.DotaUtil;

import java.text.DecimalFormat;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class HeroInspector {

    private Context context;
    private Hero hero;

    public HeroInspector(Context context, Hero hero) {
        this.context = context;
        this.hero = hero;
    }

    public String getGames() {
        return String.valueOf(hero.getGamesPlayed());
    }

    public String getWinRate() {
        float games = hero.getGamesPlayed();
        if (games == 0) return "0.00%";
        float win = hero.getGamesWon();
        float winRate = (win / games) * 100.00f;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(winRate) + "%";
    }

    public String getWinLoss() {
        int win = hero.getGamesWon();
        int lose = hero.getGamesPlayed() - win;
        return win + "/" + lose;
    }

    @BindingAdapter("heroUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        RequestOptions options = new RequestOptions().centerCrop();
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .transition(withCrossFade())
                .into(imageView);
    }

    public String getImageUrl() {
        return DotaUtil.Image.getHeroUrl(context, (int) hero.getID());
    }
}
