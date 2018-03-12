package com.nikola.jakshic.truesight.inspector;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.model.match.Match;
import com.nikola.jakshic.truesight.util.DotaUtil;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

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

    public String getWinner() {
        return context.getString(match.isRadiantWin()
                ? R.string.match_radiant_victory
                : R.string.match_dire_victory);
    }

    public int getResultColor() {
        return ContextCompat.getColor(context, match.isRadiantWin() ? R.color.match_won : R.color.match_lost);
    }

    public int getPlayerColor() {
        return ContextCompat.getColor(context, player.getPlayerSlot() <= 4 ? R.color.match_won : R.color.match_lost);
    }

    public String getRadiantScore() {
        return String.valueOf(match.getRadiantScore());
    }

    public String getDireScore() {
        return String.valueOf(match.getDireScore());
    }

    public String getGameMode() {
        return DotaUtil.Match.getMode(match.getGameMode(), "Unknown");
    }

    public String getDuration() {
        long hours, minutes, seconds;

        hours = TimeUnit.SECONDS.toHours(match.getDuration());

        if (hours > 0) {
            minutes = TimeUnit.SECONDS.toMinutes(match.getDuration() - hours * 60 * 60);
            seconds = match.getDuration() - hours * 60 * 60 - minutes * 60;
        } else {
            minutes = TimeUnit.SECONDS.toMinutes(match.getDuration());
            seconds = match.getDuration() - minutes * 60;
        }

        String min = String.valueOf(minutes);
        String sec = String.valueOf(seconds);

        if (minutes < 10)
            min = 0 + String.valueOf(minutes);
        if (seconds < 10)
            sec = 0 + String.valueOf(seconds);
        if (hours > 0)
            return hours + "h " + min + "m " + sec + "s";
        else
            return min + "m " + sec + "s";
    }

    public String getEndTime() {
        long SECOND_IN_MILLIS = 1000;
        long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
        long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
        long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
        long MONTH_IN_MILLIS = DAY_IN_MILLIS * 30;
        long YEAR_IN_MILLIS = MONTH_IN_MILLIS * 12;

        long time = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(match.getStartTime()) - TimeUnit.SECONDS.toMillis(match.getDuration());
        long minute = time / MINUTE_IN_MILLIS;
        long hour = time / HOUR_IN_MILLIS;
        long day = time / DAY_IN_MILLIS;
        long month = time / MONTH_IN_MILLIS;
        long year = time / YEAR_IN_MILLIS;

        if (year > 0)
            return context.getResources().getQuantityString(R.plurals.year, (int) year, year);
        else if (month > 0)
            return context.getResources().getQuantityString(R.plurals.month, (int) month, month);
        else if (day > 0)
            return context.getResources().getQuantityString(R.plurals.day, (int) day, day);
        else if (hour > 0)
            return context.getResources().getQuantityString(R.plurals.hour, (int) hour, hour);
        else
            return context.getResources().getQuantityString(R.plurals.minute, (int) minute, minute);
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
        if (url != null) {
            RequestOptions options = new RequestOptions().centerCrop();
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(options)
                    .transition(withCrossFade())
                    .into(imageView);
        }
    }

    public String getHeroUrl() {
        return DotaUtil.Image.getHeroUrl(context, player.getHeroId());
    }

    public String getItemUrl(long itemId) {
        return DotaUtil.Image.getItemUrl(context, itemId);
    }
}
