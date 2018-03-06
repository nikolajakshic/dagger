package com.nikola.jakshic.truesight;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.model.match.Match;
import com.nikola.jakshic.truesight.util.DotaUtil;

import java.text.DecimalFormat;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MatchDetailInspector {

    private Match match;
    private Player player;
    private Context context;

    public MatchDetailInspector(Match match, Player player, Context context) {
        this.match = match;
        this.player = player;
        this.context = context;
    }

    public String getPlayerName() {
        return context.getString(
                R.string.match_player_name,
                TextUtils.isEmpty(player.getPersonaName())
                        ? "Unknown"
                        : player.getPersonaName());
    }

    public String getHeroLevel() {
        return context.getString(R.string.match_hero_level, player.getLevel());
    }

    public String getKda() {
        return context.getString(
                R.string.match_kda,
                player.getKills(),
                player.getDeaths(),
                player.getAssists());
    }

    public String getHeroDamage() {
        return context.getString(R.string.match_hero_damage, player.getHeroDamage());
    }

    public String getTowerDamage() {
        return context.getString(R.string.match_tower_damage, player.getTowerDamage());
    }

    public String getHeroHealing() {
        return context.getString(R.string.match_hero_healing, player.getHeroHealing());
    }

    public String getHeroStuns() {
        DecimalFormat df = new DecimalFormat("0.00");
        return context.getString(R.string.match_hero_stuns, df.format(player.getStuns()));
    }

    public String getLastHits() {
        return context.getString(R.string.match_last_hits, player.getLastHits());
    }

    public String getDenies() {
        return context.getString(R.string.match_denies, player.getDenies());
    }

    public String getObservers() {
        return context.getString(R.string.match_observer_purchased, player.getPurchaseWardObserver());
    }

    public String getSentries() {
        return context.getString(R.string.match_sentry_purchased, player.getPurchaseWardSentry());
    }

    public String getGpm() {
        return context.getString(R.string.match_gpm, player.getGoldPerMin());
    }

    public String getXpm() {
        return context.getString(R.string.match_xpm, player.getXpPerMin());
    }

    public String getItem0() {
        return getItemUrl(player.getItem0());
    }

    public String getItem1() {
        return getItemUrl(player.getItem1());
    }

    public String getItem2() {
        return getItemUrl(player.getItem2());
    }

    public String getItem3() {
        return getItemUrl(player.getItem3());
    }

    public String getItem4() {
        return getItemUrl(player.getItem4());
    }

    public String getItem5() {
        return getItemUrl(player.getItem5());
    }

    public String getBackpack0() {
        return getItemUrl(player.getBackpack0());
    }

    public String getBackpack1() {
        return getItemUrl(player.getBackpack1());
    }

    public String getBackpack2() {
        return getItemUrl(player.getBackpack2());
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        RequestOptions options = new RequestOptions().centerCrop();
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .transition(withCrossFade())
                .into(imageView);
    }

    public String getHeroUrl() {
        return DotaUtil.Image.getHeroUrl(context, player.getHeroId());
    }

    public String getItemUrl(long itemId) {
        return DotaUtil.Image.getItemUrl(context, itemId);
    }
}
