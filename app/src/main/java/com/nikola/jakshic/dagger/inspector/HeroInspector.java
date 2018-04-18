package com.nikola.jakshic.dagger.inspector;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nikola.jakshic.dagger.model.Hero;
import com.nikola.jakshic.dagger.util.DotaUtil;

import java.text.DecimalFormat;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Inspector objects are used in XML files for Data Binding
 * If any changes needs to be done with Value Objects (models)
 * e.g. date is in milliseconds and needs to be converted to String
 * that is done in Inspector classes.
 */
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
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(getPercentage()) + "%";
    }

    public float getPercentage() {
        float games = hero.getGamesPlayed();
        if (games == 0) return 0;
        float win = hero.getGamesWon();
        return (win / games) * 100.00f;
    }

    public String getWinLoss() {
        int win = hero.getGamesWon();
        int lose = hero.getGamesPlayed() - win;
        return win + "/" + lose;
    }

    public String getImageUrl() {
        return DotaUtil.Image.getHeroUrl(context, (int) hero.getHeroId());
    }

    // TODO should not be in this file
    @BindingAdapter("layout_width")
    public static void setLayoutWidth(View view, float percentage) {
        int width = 85;
        final float scale = view.getContext().getResources().getDisplayMetrics().density;

        width = (int) (width * scale + 0.5f);

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (width * (percentage / 100));

        view.setLayoutParams(params);
    }

    // TODO should not be in this file
    @BindingAdapter("heroUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        if (url != null) {
            RequestOptions options = new RequestOptions().centerCrop();
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(options)
                    .transition(withCrossFade())
                    .into(imageView);
        }
    }
}