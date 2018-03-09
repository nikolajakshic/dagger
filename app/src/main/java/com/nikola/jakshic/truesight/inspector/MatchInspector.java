package com.nikola.jakshic.truesight.inspector;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.model.match.Match;
import com.nikola.jakshic.truesight.util.DotaUtil;

import java.util.concurrent.TimeUnit;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MatchInspector {

    private Match match;
    private Context context;

    public MatchInspector(Context context, Match match) {
        this.match = match;
        this.context = context;
    }

    public String getWin() {
        String result;
        if (match.isRadiantWin() && match.getPlayerSlot() <= 4)
            result = "Won";
        else if (!match.isRadiantWin() && match.getPlayerSlot() > 4)
            result = "Won";
        else
            result = "Lost";
        return result;
    }

    public int getResultColor() {
        int color;
        if (getWin().equals("Won"))
            color = ContextCompat.getColor(context, R.color.match_won);
        else
            color = ContextCompat.getColor(context, R.color.match_lost);
        return color;
    }

    public String getSkill() {
        return DotaUtil.Match.getSkill(match.getSkill(), "Unknown");
    }

    public String getLobby() {
        return DotaUtil.Match.getLobby(match.getLobbyType(), "Unknown");
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
            return hours + ":" + min + ":" + sec;
        else
            return min + ":" + sec;
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

    public String getMode() {
        return DotaUtil.Match.getMode(match.getGameMode(), "Unknown");
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

    public String getImageUrl() {
        return DotaUtil.Image.getHeroUrl(context, (int) match.getHeroId());
    }
}